package br.com.renato.berg.kartRank;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import br.com.renato.berg.vo.Register;

public class KartRank {

	private List<Register> registers = new LinkedList<Register>();

	private void includeRegister(List<String> parameters) {
		Register register = new Register();
		
		SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm:ss.SSS");
		SimpleDateFormat simpleDateFormatTimeTurn = new SimpleDateFormat("mm:ss.SSS");
		for (int pos = 0; pos < parameters.size(); pos++) {
			try {
				if (pos == 0) {
					Date time = simpleDateFormatTime.parse(parameters.get(pos));
					register.setTime(time);
				} else if (pos == 1) {
					String codeAndName = parameters.get(pos);
					register.setPilotCode(codeAndName.substring(0, codeAndName.indexOf(" ")));
					register.setPilotName(codeAndName.substring(codeAndName.lastIndexOf(" ")));
				} else if (pos == 2) {
					Integer numberOfTurn = Integer.valueOf(parameters.get(pos));
					register.setNumberOfTurn(numberOfTurn);
				} else if (pos == 3) {
					Date timeTurn = simpleDateFormatTimeTurn.parse(parameters.get(pos));
					register.setTimeTurn(timeTurn);
				} else if (pos == 4) {
					NumberFormat numberFormat = NumberFormat.getInstance(new Locale("pt", "BR"));
					Number number = numberFormat.parse(parameters.get(pos));
					register.setAverageLapSpeed(number.doubleValue());
				}
			} catch(Exception exception) { }
			
			if (register.getTime() != null && (register.getPilotCode() != null && !register.getPilotCode().isEmpty()) && (register.getPilotName() != null && !register.getPilotName().isEmpty()) && register.getNumberOfTurn() != null && register.getTimeTurn() != null && register.getAverageLapSpeed() != null) {
				registers.add(register);
			}
		}
	}

	private void rawParse(final File file) throws IOException {
		final int carriageReturn = (int) '\r';
		final int endLine = (int) '\n';
		final int endFile = -1;
		final int space = (int) ' ';
		final int tab = (int) '\t';
		StringBuffer buffer = new StringBuffer(1024);
		
		FileInputStream fileInputStream = new FileInputStream(file);
		BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
		int character;
		
		List<String> parameters = new LinkedList<String>();
		int spaceLength = 0;
		do {
			character = bufferedInputStream.read();
			
			if ((character == carriageReturn || character == endFile) && !"".equals(buffer.toString())) {
				parameters.add(buffer.toString());
			}
			
			if (character == endLine || character == endFile) {
				includeRegister(parameters);
				buffer.setLength(0);
				spaceLength = 0;
				parameters = new LinkedList<String>();
			} else {
				if (character == space || character == tab) {
					if (!"".equals(buffer.toString()) && spaceLength == 1) {
						parameters.add(buffer.toString().substring(0, buffer.length() - 1));
						buffer.setLength(0);
						spaceLength = 0;
					} else {
						if (!"".equals(buffer.toString().trim())) {
							buffer.append((char) character);
						}
						spaceLength++;
					}
				} else {
					buffer.append((char) character);
					spaceLength = 0;
				}
			}
		} while (character != endFile);
		
		bufferedInputStream.close();
		fileInputStream.close();
	}

	public void analyze() {
		SortedMap<String, Register> rank = new TreeMap<String, Register>();
		
		for (Register register : registers) {
			if (rank.containsKey(register.getPilotCode())) {
				Register newRegister = rank.get(register.getPilotCode());
				
				long sum = newRegister.getTime().getTime() + register.getTimeTurn().getTime();
				newRegister.setTime(new Date(sum));
				
				newRegister.setPilotCode(register.getPilotCode());
				
				newRegister.setPilotName(register.getPilotName());
				
				newRegister.setNumberOfTurn((register.getNumberOfTurn() > newRegister.getNumberOfTurn()) ? register.getNumberOfTurn() : newRegister.getNumberOfTurn());
				
				newRegister.setTimeTurn((register.getTimeTurn().before(newRegister.getTimeTurn())) ? register.getTimeTurn() : newRegister.getTimeTurn());
				
				newRegister.setAverageLapSpeed((register.getAverageLapSpeed() + newRegister.getAverageLapSpeed()) / 2);
				
				rank.put(register.getPilotCode(), newRegister);
			} else {
				Register newRegister = new Register();
				newRegister.setTime(register.getTimeTurn());
				newRegister.setPilotCode(register.getPilotCode());
				newRegister.setPilotName(register.getPilotName());
				newRegister.setNumberOfTurn(register.getNumberOfTurn());
				newRegister.setTimeTurn(register.getTimeTurn());
				newRegister.setAverageLapSpeed(register.getAverageLapSpeed());
				rank.put(register.getPilotCode(), newRegister);
			}
		}
		
		SortedSet<Map.Entry<String, Register>> sortedset = new TreeSet<Map.Entry<String, Register>>(new Comparator<Map.Entry<String, Register>>() {
			public int compare(Entry<String, Register> e1, Entry<String, Register> e2) {
				return e1.getValue().compareTo(e2.getValue());
			}
		});

		sortedset.addAll(rank.entrySet());
		
		SimpleDateFormat simpleDateFormatTime = new SimpleDateFormat("HH:mm:ss.SSS");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss.SSS");
		Date bestTurnRunning = null;
		
		Iterator<Map.Entry<String, Register>> rankValues = sortedset.iterator();
		for (int position = 1; rankValues.hasNext(); position++) {
			Map.Entry<String, Register> mapEntry = rankValues.next();
			System.out.println("Posição de Chegada: " + position);
			System.out.println("Código do Piloto: " + mapEntry.getValue().getPilotCode());
			System.out.println("Nome do Piloto: " + mapEntry.getValue().getPilotName());
			System.out.println("Qtde Voltas Completadas: " + mapEntry.getValue().getNumberOfTurn());
			System.out.println("Tempo Total da Prova: " + simpleDateFormatTime.format(mapEntry.getValue().getTime()));
			System.out.println("Melhor Volta: " + simpleDateFormat.format(mapEntry.getValue().getTimeTurn()));
			System.out.println("Velocidade Média: " + mapEntry.getValue().getAverageLapSpeed());
			System.out.println("-------------------------------------------");
			
			if (bestTurnRunning == null || (bestTurnRunning != null && bestTurnRunning.after(mapEntry.getValue().getTimeTurn()))) {
				bestTurnRunning = mapEntry.getValue().getTimeTurn();
			}
		}
		
		System.out.println("Melhor Volta da Corrida: " + simpleDateFormat.format(bestTurnRunning));
	}

	public void obtainData(File file) {
		try {
			rawParse(file);
		} catch (IOException exception) {
			System.out.println("Este aquivo não é um arquivo com formato válido.");
		}
	}

	public static void main(String [] args) {
		File file = null;
		
		if (args.length == 0) {
			Scanner scanner = new Scanner(System.in);
			
			String option = null;
			do {
				System.out.println("Deseja executar um novo arquivo? Caso contrário, o arquivo padrão será executado. Y/N");
				option = scanner.nextLine();
				
				if ("y".equals(option.toLowerCase())) {
					System.out.println("Informe o caminho do arquivo.");
					String path = scanner.nextLine();
					
					file = new File(path);
				} else if ("n".equals(option.toLowerCase())) {
					ClassLoader classLoader = new KartRank().getClass().getClassLoader();
					file = new File(classLoader.getResource("Rank.txt").getFile());
				}
			} while(!file.exists() || (file.exists() && file.isDirectory()));
			
			scanner.close();
		} else if (args.length == 1) {
			file = new File(args[0]);
		} else {
			System.out.println("Parâmetro inválido.");
		}
		
		if (file.exists() && file.isFile()) {
			KartRank kartRank = new KartRank();
			kartRank.obtainData(file);
			kartRank.analyze();
		} else {
			System.out.println("O arquivo não existe ou é um diretório.");
		}
	}

}
