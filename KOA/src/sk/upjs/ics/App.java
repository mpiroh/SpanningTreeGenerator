package sk.upjs.ics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

public class App {
	private static List<Vrchol> vrcholy = new ArrayList<>();
	private static List<Hrana> hrany = new ArrayList<>();
	private static int pocetVrcholov;
	private static int pocetHran;
	private static List<List<Hrana>> kostry = new ArrayList<>();
	private static List<Hrana> aktKostra = new ArrayList<>();
	private static int d;

	public static void pripravData() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("mesta2.txt"));
			pocetVrcholov = scanner.nextInt();

			for (int i = 0; i < pocetVrcholov; i++) {
				vrcholy.add(new Vrchol(i));
			}

			for (int i = 0; i < pocetVrcholov; i++) {
				for (int j = 0; j < pocetVrcholov; j++) {
					if (scanner.nextInt() == 1 && j > i) {
						hrany.add(new Hrana(vrcholy.get(i), vrcholy.get(j)));
					}
				}
			}

			pocetHran = hrany.size();
			d = stupenPrvehoVrchola();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	public static int stupenPrvehoVrchola() {
		int stupen = 0;
		for (Hrana h : hrany) {
			if (h.getV1() == vrcholy.get(0) || h.getV2() == vrcholy.get(0)) {
				stupen++;
			}
		}
		return stupen;
	}

	public static void generuj() {
		int i = -1;
		int l = 0;
		boolean odstranena = false;
		while (true) {
			if (!odstranena) {
				i++;
			} else {
				odstranena = false;
			}

			if (i >= pocetHran) {
				if (aktKostra.size() == 0) {
					return;
				}
				for (int u = 0; u < hrany.size(); u++) {
					if (hrany.get(u) == aktKostra.get(aktKostra.size()-1)) {
						l = u;
						break;
					}
				}
				odstranHranu(l);
				i = l + 1;
				odstranena = true;
			} else {
				Hrana aktHrana = hrany.get(i);
				Vrchol v1 = aktHrana.getV1();
				Vrchol v2 = aktHrana.getV2();
				if (v1.getComp() != v2.getComp()) {
					aktKostra.add(aktHrana);
					l = i;

					//System.out.println(aktHrana.getV1().getId() + "-" + aktHrana.getV2().getId());

					// ak ani jeden z vrcholov tej hrany nie je koren, tak
					// vymenime
					// koren
					if (v1.getParent() != Integer.MAX_VALUE && v2.getParent() != Integer.MAX_VALUE) {
						vymenKoren(v1, v2);
					}

					// tam kde je koren zmenime parent na ten druhy vrchol a
					// zmenime
					// vsetky komponenty v tom postrome

					if (v2.getParent() == Integer.MAX_VALUE) {
						v2.setParent(v1.getId());
						int c = v2.getComp();
						for (Vrchol v : vrcholy) {
							if (v.getComp() == c) {
								v.setComp(v1.getComp());
							}
						}
					} else if (v1.getParent() == Integer.MAX_VALUE) {
						v1.setParent(v2.getId());
						int c = v1.getComp();
						for (Vrchol v : vrcholy) {
							if (v.getComp() == c) {
								v.setComp(v2.getComp());
							}
						}
					}

					if (aktKostra.size() == pocetVrcholov - 1) {
						kostry.add(aktKostra);
						System.out.println("--------------------------------");
						for (Hrana h : aktKostra) {
							System.out.println(h.getV1().getId() + "-" + h.getV2().getId());
						}
						if (l == d - 1 && aktKostra.size() == 1) {
							break;
						} else {
							odstranHranu(l);
							i = l + 1;
							odstranena = true;
						}
					}
				}
			}
		}
	}

	public static void vymenKoren(Vrchol vrchol1, Vrchol vrchol2) {
		Map<Integer, Integer> mapa = new HashMap<>();
		Vrchol v1 = vrchol1;
		Vrchol v2 = vrchol2;
		while (true) {
			if (v2.getParent() == Integer.MAX_VALUE) {
				break;
			}
			mapa.put(v2.getParent(), v2.getId());
			v2 = dajVrcholPodlaId(v2.getParent());
		}

		for (int i : mapa.keySet()) {
			dajVrcholPodlaId(i).setParent(mapa.get(i));
		}
		vrchol2.setParent(v1.getId());
	}

	public static Vrchol dajVrcholPodlaId(int id) {
		for (Vrchol v : vrcholy) {
			if (v.getId() == id) {
				return v;
			}
		}
		return null;
	}

	public static void odstranHranu(int l) {
		Queue<Integer> rad = new LinkedList<>();
		Hrana odstHrana = hrany.get(l);
		aktKostra.remove(odstHrana);

		Vrchol v1 = odstHrana.getV1();
		Vrchol v2 = odstHrana.getV2();
		if (v1.getParent() == v2.getId()) {
			v1.setComp(v1.getId());
			v1.setParent(Integer.MAX_VALUE);
			rad.add(v1.getId());
			while (!rad.isEmpty()) {
				int p = rad.poll();
				for (Vrchol v : vrcholy) {
					if (v.getParent() == p) {
						v.setComp(v1.getComp());
						rad.add(v.getId());
					}
				}
			}
		} else if (v2.getParent() == v1.getId()) {
			v2.setComp(v2.getId());
			v2.setParent(Integer.MAX_VALUE);
			rad.add(v2.getId());
			while (!rad.isEmpty()) {
				int p = rad.poll();
				for (Vrchol v : vrcholy) {
					if (v.getParent() == p) {
						v.setComp(v2.getComp());
						rad.add(v.getId());
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		pripravData();
		generuj();

		//System.out.println("KOSTRY");
		//for (List<Hrana> kostra : kostry) {
		//	for (Hrana h : kostra) {
		//		System.out.println(h.getV1().getId() + "-" + h.getV2().getId());
		//	}
		//	System.out.println("-----");
		//}
	}
}
