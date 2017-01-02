import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class Linker {
	public static void main(String[] args) {
		int sizeOfMods = 0;
		File file = new File(args[0]);
				try {
					Scanner sc = new Scanner(file);
					ArrayList<Symbol> list = new ArrayList<Symbol>();
					while (sc.hasNext()) {
						int numMods = sc.nextInt();
						for (int i = 0; i < numMods; i++) {
							int numDefs = sc.nextInt();
							for (int j = 0; j < numDefs; j++) {
								Symbol symbol = new Symbol(sc.next(), (sizeOfMods + sc.nextInt()));
								list.add(symbol);
								for (int k = 0; k < list.size() - 1; k ++) { //checks list of symbols for duplicate
									if (symbol.getName().equals(list.get(k).getName())) {
										System.out.println("Error: symbol " + list.get(k).getName() + " already defined");
										list.remove(list.size() - 1);
									}
								}
							}
							int numUses = sc.nextInt();
							for (int j = 0; j < numUses; j ++) {
								sc.next();
							}
							int numTexts = sc.nextInt();
							for (int j = 0; j < list.size(); j ++) { // checks if symbol larger than module
								if (list.get(j).getAbsoluteAddress() - sizeOfMods > numTexts) {
									System.out.println("Error: symbol " + list.get(j).getName() + " exceeds the size of the module. It has been assigned 0 (relative)");
									list.get(j).setAbsoluteAddress(sizeOfMods);
								}
							}
							for (int j = 0; j < numTexts; j ++) {
								sc.next();
							}
							sizeOfMods += numTexts;
						}
					}
					for (int i = 0; i < list.size(); i ++) {
						System.out.print(list.get(i).getName() + " " + list.get(i).getAbsoluteAddress() + " ");
					}
					System.out.println();
					sc.close();
					// END OF PASS 1
					sc = new Scanner(file);
					sizeOfMods = 0;
					while (sc.hasNextLine()) {
						int numMods = sc.nextInt();
						for (int i = 0; i < numMods; i++) {
							int numDefs = sc.nextInt();
							for (int j = 0; j < numDefs * 2; j++) {
								sc.next();
							}
							int numUses = sc.nextInt();
							for (int j = 0; j < list.size(); j ++) {
								list.get(j).definedInUse(false); //default not defined in uses section
							}
							for (int j = 0; j < numUses; j ++) {
								String use = sc.next();
								boolean isFound = false;
								for (int k = 0; k < list.size(); k ++) {
									if (list.get(k).getName().equals(use)) {
										list.get(k).definedInUse(true); // if the symbol IS defined in uses section
										list.get(k).setLocalAddress(j);
										isFound = true;
										list.get(k).used();
									}
								}
								if (!isFound) { // checks if used symbol is defined
									System.out.println("Error: symbol " + use + " not defined. It has been given a value of 0");
									Symbol sym = new Symbol(use, 0);
									sym.used();
									sym.setLocalAddress(j);
									list.add(sym);
								}
							}
							for (int j = 0; j < list.size(); j ++) { // checks if defined symbol is used
								if (!list.get(j).isUsed()) {
									System.out.println("Warning: symbol " + list.get(j).getName() + " is defined but not used");
									list.get(j).used();
								}
							}
							int numTexts = sc.nextInt();
							for (int j = 0; j < numTexts; j ++) {
								int text = sc.nextInt();
								int output = 0;
								if (text % 10 == 1) {
									output = text / 10;
								} else if (text % 10 == 2) {
									if (((text / 10) % 1000) < 600) {
										output = text / 10;
									} else {
										System.out.println("Error: absolute address exceeds machine size. 0 used");
										output = text / 10000 * 1000;
									}
								} else if (text % 10 == 3) {
									if (((text / 10) % 1000) < numTexts) {
										output = (text / 10) + sizeOfMods;
									} else {
										System.out.println("Error: relative address exceeds module size. 0 used");
										output = text / 10000 * 1000;
									}
								} else if ((text % 10 == 4) && (((text / 10) % 1000) < numUses)) {
									Symbol sym = new Symbol();
									for (int k = 0; k < list.size(); k ++) {
										if (list.get(k).getLocalAddress() == (text / 10) % 1000) {
											sym = list.get(k);
											list.get(k).usedInModule(true);
										}
									}
									output = sym.getAbsoluteAddress() + ((text / 1000) * 100);
								} else {
									System.out.println("Error: external address too large to reference entry in use list. Treating address as immediate");
									output = text / 10;
								}
								
								for (int k = 0; k < list.size(); k ++) {
									if (list.get(k).isDefinedInUse() && (!list.get(k).isUsedInModule()) && (j == numTexts - 1)) {
										System.out.println("Warning: symbol " + list.get(k).getName() + " is defined in the uses section but is not used in the module");
									}
								}
								
								System.out.println(output);
							}
							for (int k = 0; k < list.size(); k ++) {
								list.get(k).usedInModule(false);
							}
							sizeOfMods += numTexts;
						}
					}
					sc.close();
				} 
		catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}
	
}
