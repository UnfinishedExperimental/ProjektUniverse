/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.hud;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.media.opengl.GL;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import universetournament.client.rendering.ressourcen.resmanagment.*;

/**
 *
 * @author dheinrich
 */
public class TextureAtlas implements Serializable
{
    private static final Logger logger = Logger.getLogger(TextureAtlas.class.
            getName());
    transient private HashMap<String, TextureAtlasElement> elements;
    transient private TextureContainer texture;
    private String file;

    public TextureAtlas(String file) {
        this.file = file;
        ini();
    }

    private void ini() {
        elements = new HashMap<String, TextureAtlasElement>();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);
            Node main = doc.getFirstChild();
            NamedNodeMap nnm = main.getAttributes();
            String texname = main.getAttributes().getNamedItem("Name").getNodeValue();
            texture = RessourcesLoader.getInstance().loadTexture(new TextureLoadJob(
                    texname, GL.GL_LINEAR, GL.GL_CLAMP_TO_EDGE));
            int width = Integer.parseInt(
                    nnm.getNamedItem("Width").getNodeValue());
            int heigth = Integer.parseInt(nnm.getNamedItem("Height").
                    getNodeValue());

            NodeList nl = main.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node node =  nl.item(i);
                NamedNodeMap nmm = node.getAttributes();
                if(nmm == null)
                    continue;
                String name = nmm.getNamedItem("Name").getNodeValue();
                int x = Integer.parseInt(nmm.getNamedItem("X").getNodeValue());
                int y = Integer.parseInt(nmm.getNamedItem("Y").getNodeValue());
                int w = Integer.parseInt(
                        nmm.getNamedItem("Width").getNodeValue());
                int h = Integer.parseInt(
                        nmm.getNamedItem("Height").getNodeValue());

                TextureAtlasElement tae = new TextureAtlasElement((float)x / width,
                                                                  (float)y / heigth,
                                                                  (float)w / width,
                                                                  (float)h / heigth,
                                                                  name);
                elements.put(name, tae);
            }
        } catch (SAXException ex) {
            logger.log(Level.SEVERE, "XML parse error eines Texture Atlases", ex);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);

        } catch (ParserConfigurationException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
    }


    public TextureAtlasElement getElement(String name) {
        TextureAtlasElement e = elements.get(name);
        if (e == null)
            logger.log(Level.WARNING, "nicht existierendes TexAtlass Element angefordert.("
                    + name + ")");
        return e;
    }

    public Collection<TextureAtlasElement> getElements() {
        return elements.values();
    }

    public TextureContainer getTexContainer() {
        return texture;
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        ini();
    }

    public static void main(String[] args) {
        new TextureAtlas("Textures/hud/hud00.xml");
    }
}
