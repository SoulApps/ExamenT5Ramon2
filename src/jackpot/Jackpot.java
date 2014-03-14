package jackpot;

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import java.sql.Date;
import java.util.Calendar;

/**
 *
 * @author Ramón Guardia López
 */
public class Jackpot {

    //Vamos a comenzar con la declaración de variables para el programa de la máquina tragaperras.
    //Creamos variables estáticas para poder usarlas después en las ventanas de una manera más clara.
    
    
    
    private int[] resultado = new int[3];
    private float saldo = 0;
    private float deposito = 1000;
    private final float VALOR_APUESTA = 0.50F;
    private float premio;

    private final int CEREZA = 0;
    private final int CAMPANA = 1;
    private final int TREBOL = 2;
    private final int MONEDA = 3;
    private final int SIETE = 4;

    private ArrayList<String> XMLData = new ArrayList();
    public static Document documento;
    
    private  Calendar fecha = Calendar.getInstance();
    
    

    /**
     * El siguiente método lo que hace es crear un número aleatorio. Ese número
     * va a servir para comenzar a jugar y bajar las apuestas.
     *
     * @return El premio a conseguir.
     */
    public float jugar() {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            resultado[i] = random.nextInt(5);
        }
        saldo -= VALOR_APUESTA;
        premio = getPremio();
        saldo += premio;
        return premio;
    }

    /**
     *
     * @param indice
     * @return
     */
    public ImageIcon getImagen(int indice) {
        try {
            ImageIcon imagen = new ImageIcon(getClass().getResource("/imagenes/" + resultado[indice] + ".png"));
            return imagen;
        } catch (NullPointerException ex) {
            Logger.getLogger(Jackpot.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private int getPremio() {
        if (resultado[0] == CEREZA && resultado[1] == CEREZA) {
            return 5;
        } else if (resultado[0] == CEREZA) {
            return 1;
        } else if (resultado[0] == CAMPANA && resultado[1] == CAMPANA && resultado[2] == CAMPANA) {
            return 10;
        } else if (resultado[0] == CAMPANA && resultado[1] == CAMPANA && resultado[2] == SIETE) {
            return 10;
        } else if (resultado[0] == TREBOL && resultado[1] == TREBOL && resultado[2] == TREBOL) {
            return 15;
        } else if (resultado[0] == TREBOL && resultado[1] == TREBOL && resultado[2] == SIETE) {
            return 15;
        } else if (resultado[0] == MONEDA && resultado[1] == MONEDA && resultado[2] == MONEDA) {
            return 20;
        } else if (resultado[0] == MONEDA && resultado[1] == MONEDA && resultado[2] == SIETE) {
            return 20;
        } else if (resultado[0] == SIETE && resultado[1] == SIETE && resultado[2] == SIETE) {
            return 100;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param valor
     */
    public void introducirMoneda(float valor) {
        this.saldo += valor;
        deposito += valor;
    }

    /**
     *
     * @return
     */
    public float getSaldo() {
        return saldo;
    }

    /**
     *
     * @return
     */
    public float getDeposito() {
        return deposito;
    }

    /**
     *
     */
    public void cobrar() {
        deposito -= saldo;
        saldo = 0;
    }

    public  ArrayList<String> getXMLData() {
        this.XMLData = XMLData;
        
        
        
        XMLData.add(String.valueOf(saldo));
        XMLData.add(String.valueOf(deposito));
        XMLData.add(String.valueOf(premio));
        XMLData.add(String.valueOf(fecha.getTime()));
        return XMLData;
    }

    public  Document getDocument() {
        try {
            DocumentBuilderFactory fábricaCreadorDocumento = DocumentBuilderFactory.newInstance();

            DocumentBuilder creadorDocumento = fábricaCreadorDocumento.newDocumentBuilder();

            Document documento = creadorDocumento.newDocument();

            //Nodos
            //Raiz
            Element elementoRaiz = documento.createElement("tragaperras");
            documento.appendChild(elementoRaiz);
            
            Element elementoJugada = documento.createElement("jugada");
            elementoRaiz.appendChild(elementoJugada);
            
            //Añadir el contenido del ArrayList al XML.
            
            //He intentado que en cada posición aparezca lo que hay en el método
            
            XMLData.add(String.valueOf(saldo));
            XMLData.add(String.valueOf(deposito));
            XMLData.add(String.valueOf(premio));
            XMLData.add(String.valueOf(fecha.getTime()));
            
            for(int i=0; i<XMLData.size(); i++ ) {
            //saldo
            Element elementoSaldo = documento.createElement("saldo");
            elementoJugada.appendChild(elementoSaldo);
            Text textoSaldo = documento.createTextNode(String.valueOf(XMLData.get(0)));
            elementoSaldo.appendChild(textoSaldo);

            //deposito
            Element elementoDeposito = documento.createElement("deposito");
            elementoJugada.appendChild(elementoDeposito);
            Text textoDeposito = documento.createTextNode(String.valueOf(XMLData.get(1)));
            elementoDeposito.appendChild(textoDeposito);
            
            //premio
            
            Element elementoPremio = documento.createElement("premio");
            elementoJugada.appendChild(elementoPremio);
            Text textoPremio = documento.createTextNode(String.valueOf(XMLData.get(2)));
            elementoPremio.appendChild(textoPremio);
            
            //fecha
            
            Element elementoFecha = documento.createElement("fecha");
            elementoJugada.appendChild(elementoFecha);
            Text textoFecha = documento.createTextNode(String.valueOf(XMLData.get(3)));
            elementoPremio.appendChild(textoFecha);
            }
            //Generar el XML.
            TransformerFactory fábricaTransformador = TransformerFactory.newInstance();
            Transformer transformador = fábricaTransformador.newTransformer();
            
            transformador.setOutputProperty(OutputKeys.INDENT, "yes");
            
            transformador.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "3");
            Source origen = new DOMSource(documento);
            Result destino = new StreamResult("datos_tragaperra.xml");
            transformador.transform(origen, destino);
            

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Jackpot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(Jackpot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(Jackpot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documento;
    }

}
