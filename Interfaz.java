package Graficación;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

public class Interfaz extends JFrame {

    private static final int PX_POR_UNIDAD = 2;

    private final JSpinner spInferior = new JSpinner(new SpinnerNumberModel(10, 0, 50, 1));
    private final JSpinner spSuperior = new JSpinner(new SpinnerNumberModel(5, 0, 50, 1));
    private final JComboBox<String> cbOrientacion = new JComboBox<>(new String[] { "Horizontal", "Vertical" });

    private final LienzoGL panelHoja = new LienzoGL() {
        @Override
        void dibujar(GL2 gl, int w, int h) {
            gl.glColor3f(0f, 0f, 1f);
            rectangulo(gl, 0, 0, w, h);

            int superior = (int) spSuperior.getValue() * PX_POR_UNIDAD;
            int inferior = (int) spInferior.getValue() * PX_POR_UNIDAD;
            gl.glColor3f(1f, 0f, 0f);
            linea(gl, 0, superior, w, superior);
            linea(gl, 0, h - 1 - inferior, w, h - 1 - inferior);
        }
    };

    private final LienzoGL panelOrientacion = new LienzoGL() {
        @Override
        void dibujar(GL2 gl, int w, int h) {
            gl.glColor3f(1f, 0f, 0f);
            if ("Horizontal".equals(cbOrientacion.getSelectedItem())) {
                rectangulo(gl, 0, 0, w - 10, h);
            } else {
                rectangulo(gl, 0, 0, 118, h);
            }
        }
    };

    public Interfaz() {
        setTitle("");
        setSize(650, 540);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblHoja = new JLabel("Hoja");
        lblHoja.setBounds(55, 62, 60, 20);
        add(lblHoja);

        panelHoja.setBounds(27, 94, 118, 164);
        add(panelHoja);

        JLabel lblInferior = new JLabel("Margen inferior");
        lblInferior.setBounds(190, 62, 120, 20);
        add(lblInferior);

        spInferior.setBounds(190, 97, 62, 28);
        add(spInferior);

        JLabel lblSuperior = new JLabel("Margen superior");
        lblSuperior.setBounds(190, 176, 120, 20);
        add(lblSuperior);

        spSuperior.setBounds(190, 211, 62, 28);
        add(spSuperior);

        JLabel lblOrientacion = new JLabel("Orientación de página.");
        lblOrientacion.setBounds(373, 62, 220, 20);
        add(lblOrientacion);

        cbOrientacion.setBounds(373, 96, 230, 26);
        add(cbOrientacion);

        panelOrientacion.setBounds(363, 140, 240, 118);
        add(panelOrientacion);

        JButton btnInicializar = new JButton("Inicializar");
        btnInicializar.setBounds(54, 324, 191, 26);
        add(btnInicializar);

        spInferior.addChangeListener(e -> panelHoja.repaint());
        spSuperior.addChangeListener(e -> panelHoja.repaint());
        cbOrientacion.addActionListener(e -> panelOrientacion.repaint());

        btnInicializar.addActionListener(e -> {
            spInferior.setValue(0);
            spSuperior.setValue(0);
            cbOrientacion.setSelectedItem("Vertical");
        });

        Font negrita = lblHoja.getFont().deriveFont(Font.BOLD);
        for (JLabel l : new JLabel[] { lblHoja, lblInferior, lblSuperior, lblOrientacion }) {
            l.setFont(negrita);
        }
    }

    private abstract static class LienzoGL extends GLJPanel implements GLEventListener {

        LienzoGL() {
            super(new GLCapabilities(GLProfile.get(GLProfile.GL2)));
            addGLEventListener(this);
        }

        abstract void dibujar(GL2 gl, int w, int h);

        @Override
        public void init(GLAutoDrawable d) {
            GL2 gl = d.getGL().getGL2();
            gl.glClearColor(0.933f, 0.933f, 0.933f, 1f);
        }

        @Override
        public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {
            GL2 gl = d.getGL().getGL2();
            gl.glViewport(0, 0, w, h);
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glOrtho(0, w, h, 0, -1, 1);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();
        }

        @Override
        public void display(GLAutoDrawable d) {
            GL2 gl = d.getGL().getGL2();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT);
            gl.glLineWidth(1f);
            dibujar(gl, d.getSurfaceWidth(), d.getSurfaceHeight());
        }

        @Override
        public void dispose(GLAutoDrawable d) {
        }

        static void rectangulo(GL2 gl, int x, int y, int w, int h) {
            float x1 = x + 0.5f, y1 = y + 0.5f, x2 = x + w - 0.5f, y2 = y + h - 0.5f;
            gl.glBegin(GL2.GL_LINE_LOOP);
            gl.glVertex2f(x1, y1);
            gl.glVertex2f(x2, y1);
            gl.glVertex2f(x2, y2);
            gl.glVertex2f(x1, y2);
            gl.glEnd();
        }

        static void linea(GL2 gl, int x1, int y1, int x2, int y2) {
            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2f(x1, y1 + 0.5f);
            gl.glVertex2f(x2, y2 + 0.5f);
            gl.glEnd();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Interfaz().setVisible(true));
    }
}