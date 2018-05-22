
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectOutputStream;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TypingTeacher extends JFrame {

	//linhas do teclado
	private String[] primeiraLinha = { "~", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "+", "<<" };
	private String[] segundaLinha = { "Tab", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\" };
	private String[] terceiraLinha = { "Caps", "A", "S", "D", "F", "G", "H", "J", "K", "L", ":", "*", "Enter" };
	private String[] quartaLinha = { "Shift", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "?", "    ", "↑" };
	private String[] quintaLinha = { "   ", "←", "↓", "→" };
	private Map<String, JButton> buttons = new HashMap<>();
	private String dadoArquivoEscrito = "";
	JTextArea text = new JTextArea();
	private String[] phrases= { "Um pequeno jabuti xereta viu dez cegonhas felizes",
			"Gazeta publica hoje breve nota de faxina na quermesse", "Quem traz CD, LP, fax, engov e whisky JB?",
			"Jovem ex-quenga picha frase da Blitz" };
	
	private int actualPhrase = 0;

	public TypingTeacher() {
		JLabel label1 = new JLabel(
				"Type some text using your keyboard. The keys you press will be highlighted and the text will be displayed");
		JLabel label2 = new JLabel("Note: Clicking the buttons with your mouse will not perform any action");
		JLabel label3 = new JLabel("Try insert the text: " + phrases[actualPhrase]);

		// frases a serem digitadas
		System.out.println("As frases para você escrever são: ");
		for (int j = 0; j < phrases.length; j++) {
			System.out.println(phrases[j]);
		}

		text.addKeyListener(new KeyListener() {

			// necessario ter os tres metodos
			@Override
			public void keyTyped(KeyEvent e) {

			}
			//voltar a cor original
			@Override
			public void keyReleased(KeyEvent e) {
				String key = getKey(e.getKeyCode(), e.getKeyChar());
				if (buttons.containsKey(key)) {
					JButton button = buttons.get(key);
					button.setBackground(new JButton().getBackground());
				}
				//ENTER limpa o textArea
				if (e.getKeyCode() == 10) {

					text.setText("");
				}
			}

			//metodo getKey, verifica se o botao foi precionado
			@Override
			public void keyPressed(KeyEvent e) {
				String key = getKey(e.getKeyCode(), e.getKeyChar());
				if (buttons.containsKey(key)) {
					JButton button = buttons.get(key);
					button.setBackground(Color.MAGENTA);
				} else {// mostrar qual key nao esta aparecendo, se ela nao existe no teclado ela aparece
					System.err.println("Nao tem key para " + e.getKeyCode() + " - " + e.getKeyChar());
				}
				//se ENTER for precionado:
				//existe a frase certa(certa)
				//e existe os acertos que o usuario fez na frase(hits)
				//os espaços em branco sao somados aos hits...
				if (e.getKeyCode() == 10) {
					String certa = phrases[actualPhrase];
					int erros = compare(certa, text.getText());
					int hits = certa.length() - erros;
					label1.setText("Hits: " + hits + " | Errors: " + erros);
					dadoArquivoEscrito += text.getText() + "\n";
					System.out.printf("A frase certa é %s com %d acertos e %d erros", certa, hits, erros);
					if (actualPhrase + 1 < phrases.length) {
						actualPhrase++;
						label2.setText("Next text: " + phrases[actualPhrase]);
						label3.setText("Type the text now");

					} else {
						label3.setText("File Saved.You finished all the texts!");
						label2.setText("Reset to try again.");
						recordData(dadoArquivoEscrito, "nervoso.txt");
					}
				}
			}
		});

		 // adicionando os panel
		JPanel topo = new JPanel();
		JPanel centro = new JPanel();
		JPanel teclado = new JPanel();
		JPanel panel = new JPanel();
		
		//arrumando o layouts
		add(topo, BorderLayout.NORTH);
		add(panel);
		add(centro, BorderLayout.CENTER);
		add(teclado, BorderLayout.SOUTH);
		topo.setLayout(new BorderLayout());
		topo.add(label1, BorderLayout.NORTH);
		topo.add(label2, BorderLayout.WEST);
		topo.add(label3, BorderLayout.SOUTH);
		centro.setLayout(new BorderLayout());
		centro.add(text, BorderLayout.WEST);
		centro.add(text, BorderLayout.CENTER);
		teclado.setLayout(new GridLayout(5, 1)); // 5 linhas 1 coluna

		// fazendo os botoes

		JButton[] buttonsPrimeiraLinha;
		buttonsPrimeiraLinha = new JButton[primeiraLinha.length];
		JButton[] buttonsSegundaLinha;
		buttonsSegundaLinha = new JButton[segundaLinha.length];
		JButton[] buttonsTerceiraLinha;
		buttonsTerceiraLinha = new JButton[terceiraLinha.length];
		JButton[] buttonsQuartaLinha;
		buttonsQuartaLinha = new JButton[quartaLinha.length];
		JButton[] buttonsQuintaLinha;
		buttonsQuintaLinha = new JButton[quintaLinha.length];

		JPanel proximaLinha = new JPanel(new GridLayout(1, primeiraLinha.length));

		// criando os botoes

		// -----------primeira Linha
		proximaLinha = new JPanel(new GridLayout(1, primeiraLinha.length));
		for (int i = 0; i < primeiraLinha.length; i++) {

			JButton botoes = new JButton(primeiraLinha[i]);
			botoes.setPreferredSize(new Dimension(80, 50));// tamanho padrão de todos os botoes
			buttonsPrimeiraLinha[i] = botoes;
			proximaLinha.add(buttonsPrimeiraLinha[i]);
			buttons.put(primeiraLinha[i], botoes);

		}
		teclado.add(proximaLinha);

		// ----------segunda Linha
		proximaLinha = new JPanel(new GridLayout(1, segundaLinha.length));

		for (int i = 0; i < segundaLinha.length; i++) {

			JButton botoes = new JButton(segundaLinha[i]);
			buttonsSegundaLinha[i] = botoes;
			proximaLinha.add(buttonsSegundaLinha[i]);
			buttons.put(segundaLinha[i], botoes);

		}
		teclado.add(proximaLinha);

		// ----------terceira Linha
		proximaLinha = new JPanel(new GridLayout(1, terceiraLinha.length));
		for (int i = 0; i < terceiraLinha.length; i++) {

			JButton botoes = new JButton(terceiraLinha[i]);
			buttonsTerceiraLinha[i] = botoes;
			proximaLinha.add(buttonsTerceiraLinha[i]);
			buttons.put(terceiraLinha[i], botoes);

		}
		teclado.add(proximaLinha);

		// ------------quarta Linha
		proximaLinha = new JPanel(new GridLayout(1, quartaLinha.length));
		for (int i = 0; i < quartaLinha.length; i++) {

			JButton botoes = new JButton(quartaLinha[i]);
			buttonsQuartaLinha[i] = botoes;
			proximaLinha.add(buttonsQuartaLinha[i]);
			buttons.put(quartaLinha[i], botoes);

			// espaco entre os botoes (boa tentativa)
			if (i == 11) {
				botoes.setVisible(false);
			}
		}

		proximaLinha.add(new JPanel()); // o quinto é diferente dos demais
		teclado.add(proximaLinha);

		// -----------quinta  LINHAA
		for (int i = 0; i < quintaLinha.length; i++) {// como fazer as setas certas???

			if (i == 0) {

				JButton botoes = new JButton(quintaLinha[i]);
				buttonsQuintaLinha[i] = botoes;
				// desenhar o botao
				Container espaco = (Container) getRootPane().getGlassPane();
				espaco.setVisible(true);
				espaco.setLayout(new GridBagLayout());
				GridBagConstraints gridBag = new GridBagConstraints();
				// posicao
				gridBag.weightx = 1;
				gridBag.weighty = 1;
				// localizacao
				gridBag.insets = new Insets(getBounds().y - 350, 180, 0, 20);
				gridBag.anchor = GridBagConstraints.SOUTHWEST;
				botoes.setPreferredSize(new Dimension(500, 50));
				espaco.add(buttonsQuintaLinha[i], gridBag);
				buttons.put(quintaLinha[i], botoes);
				JPanel espacoo = new JPanel();
				proximaLinha.add(espacoo);
				//aonde coloco as setas...?
			}
			buttonsQuintaLinha[i] = new JButton(quintaLinha[i]);
			proximaLinha.add(buttonsQuintaLinha[i]);
		}
	}// end construtor

	//Colocando as key especiais para retornar as key especiais
	//pois elas nao sao reconhecidas
	private String getKey(int keyCode, char charCode) {
		switch (keyCode) {
		case 10:
			return "Enter";
		case 32:
			return "   ";
		case 8:
			return "<<";
		case 9:
			return "Tab";
		case 20:
			return "Caps";
		case 37:
			return "←";
		case 38:
			return "↑";
		case 39:
			return "→";
		case 40:
			return "↓";
		case 16:
			return "Shift";
		case 111:
			return "\\";

		default:
			return (charCode + "").toUpperCase();
		}// end switch
	}// end getKey()
	
	// comparar Strings,mostrar certos e errados
	// lendo string do TextArea e comparando com a frase certa
	private int compare(String certa, String tentativa) {
		char[] c = certa.toCharArray();
		char[] t = tentativa.toCharArray();
		int erros = 0;
		// erros += Math.abs(certa.length() - tentativa.length());
		for (int i = 0; i < c.length; i++) {
			if (i >= t.length)
				break;
			if (c[i] != t[i])
				++erros;
		}
		return erros;
	}
	//Salvar arquivo.txt
	//o arquivo e salvo no no scr
	private void recordData(String dados, String arquivo) {
		try {
			FileOutputStream stream = new FileOutputStream(arquivo);
			stream.write(dados.getBytes());
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}// end class
