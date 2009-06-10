package tajmi.instances.cdk.som;

import com.sun.opengl.util.Animator;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLJPanel;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import scala.Tuple2;
import tajmi.Util;
import tajmi.abstracts.som.InitFunc;
import tajmi.abstracts.som.ViewField;
//import tajmi.som.DisplayField;
import tajmi.som.Field;
import tajmi.som.Position;

/**
 *
 * @author guest
 */
public class OpenGLFieldView extends ViewField {

    public static void main(String[] args) throws FileNotFoundException, IOException, CDKException {
        new OpenGLFieldView().start();
    }

    void start() throws FileNotFoundException, IOException, CDKException {
//        InitFunc<Integer, Integer> initf = new InitFunc<Integer, Integer>() {
//
//            @Override
//            public Integer call() {
//                return 42;
//            }
//        };
        String root = "test-data" + File.separator + "hiv1-inhibitors";
        File[] files = new File(root).listFiles(new FileFilter() {

            int count = 0;
            public boolean accept(File pathname) {
                return count++ < 9;
            }
        });
        List<IAtomContainer> molecules = new LinkedList();
        for (File f : files) {
            IMolecule m = Util.readMoleculeFile(f.getAbsolutePath());
            m.setID(f.getName());
            molecules.add(m);
        }

        InitFunc initf = new AtomContainerInitFunc().params(molecules, new Random(42));

        Field<FieldModel<IAtomContainer>> f = new Field(9, 9, initf);
        for (Tuple2<Position, FieldModel<IAtomContainer>> m : f) {
            m._2().setGeneralizeMedian(m._2().peek());
        }
        params(f).call();
    }

    @Override
    public Object call() {
        Field<FieldModel<IAtomContainer>> f = getField();
        int length = (Integer) f.dimensions()._1();
        int width = (Integer) f.dimensions()._2();
        DisplayField df = new DisplayField();
        df.setDimensions(length, width);
        df.setField(f);
        df.displayFrame("SOM");
        return f;

    }

    public class DisplayField implements GLEventListener, MouseListener, MouseMotionListener {

    public DisplayField(){
//        Float[] red = {255f, 0f, 0f};
//        Float[] green = {0f, 255f, 0f};
//        Float[] yellow = {255f, 255f, 0f};
//        Float[] blue = {0f, 0f, 255f};
//        Float[] purple = {128f, 0f, 128f};
//        Float[] violet = {148f, 0f, 211f};
//        Float[] pink = {255f, 20f, 147f};
//        Float[] white = {255f, 255f, 255f};
//        Float[] grey = {169f, 169f, 169f};
//        Float[] cyan = {0f, 255f, 255f};
//        Float[] brown = {139f, 69f, 19f};
//        Float[] orange = {255f, 128f, 0f};
//        colorRGBs.put(Color.RED, red);
//        colorRGBs.put(Color.GREEN, green);
//        colorRGBs.put(Color.YELLOW, yellow);
//        colorRGBs.put(Color.BLUE, blue);
//        colorRGBs.put(Color.PURPLE, purple);
//        colorRGBs.put(Color.VIOLET, violet);
//        colorRGBs.put(Color.PINK, pink);
//        colorRGBs.put(Color.WHITE, white);
//        colorRGBs.put(Color.GREY, grey);
//        colorRGBs.put(Color.CYAN, cyan);
//        colorRGBs.put(Color.BROWN, brown);
//        colorRGBs.put(Color.ORANGE, orange);


        random_gen = new Random(42);
    }

    Random random_gen;
//    enum Color {
//        RED, GREEN, YELLOW, BLUE, PURPLE, VIOLET, PINK, WHITE, GREY, CYAN, BROWN, ORANGE
//    }

    private int length;
    private int width;
    private Field<FieldModel<IAtomContainer>> field;
//    private int list1;
    private Map<Integer, Float[]> colorRGBs = new Hashtable<Integer, Float[]>();

    private java.util.List<Integer> gl_list_pointers;


    public void setDimensions(int length, int width) {
        this.length = length;
        this.width = width;
    }

    public void setField(Field<FieldModel<IAtomContainer>> field) {
        this.field = field;
    }

    public Field<FieldModel<IAtomContainer>> getField() {
        return field;
    }

    public void displayFrame(String title) {
        Frame frame = new Frame(title);
        GLCanvas canvas = new GLCanvas();
        canvas.addGLEventListener(this);
        frame.add(canvas);
        frame.setSize(length, width);
        final Animator animator = new Animator(canvas);
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
                        animator.stop();
                        System.exit(0);
                    }
                }).start();
            }
        });

        frame.setVisible(true);
        animator.start();
    }

    public void init(GLAutoDrawable drawable) {
        GL gl = drawable.getGL();
        gl.setSwapInterval(1);

        float pos[] = {5.0f, 5.0f, 10.0f, 0.0f};
        // Creating color vectors to change colors of points
        float red[] = {0.8f, 0.1f, 0.0f, 1.0f};
        float green[] = {0.0f, 0.8f, 0.2f, 1.0f};
        float blue[] = {0.2f, 0.2f, 1.0f, 1.0f};

        gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, pos, 0);
        gl.glEnable(GL.GL_CULL_FACE);
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_LIGHT0);
        gl.glEnable(GL.GL_DEPTH_TEST);

        /* make the list of points */


        gl_list_pointers = draw_me_dots(gl);
        gl.glEnable(GL.GL_NORMALIZE);
        drawable.addMouseListener(this);
        drawable.addMouseMotionListener(this);
    }

    public void display(GLAutoDrawable drawable) {

        GL gl = drawable.getGL();

        // Special handling for the case where the GLJPanel is translucent
        // and wants to be composited with other Java 2D content
        if ((drawable instanceof GLJPanel) &&
                !((GLJPanel) drawable).isOpaque() &&
                ((GLJPanel) drawable).shouldPreserveColorBufferIfTranslucent()) {
            gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
        } else {
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        }

        for (Integer pointer : gl_list_pointers)
            gl.glCallList(pointer.intValue());

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL gl = drawable.getGL();

        float h = (float) height / (float) width;

        gl.glMatrixMode(GL.GL_PROJECTION);

        System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
        System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
        System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));
        gl.glLoadIdentity();
        gl.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -50.0f);
    }

    public void displayChanged(GLAutoDrawable drawable, boolean arg1, boolean arg2) {
    }


    public void createLists(GL gl){
        gl.glPushMatrix();
       // gl.glPointSize((float) 4.0);
        gl.glTranslated((-length/2)+0.5, -width/2, 0);
        gl.glBegin(gl.GL_POINTS);
        gl.glPointSize((float) 2.0);

                gl.glEnd();
        gl.glPopMatrix();


    }

    private java.util.List<Integer> draw_me_dots (GL gl) {
        Map<Integer, java.util.List<Tuple2<Position, FieldModel<IAtomContainer>>>>
                assigned_colors = color_me_dots();

        java.util.List<Integer> dot_pointers = new LinkedList();
        for (Integer color_id : assigned_colors.keySet()) {
            int dots = gl.glGenLists(1);
            gl.glNewList(dots, GL.GL_COMPILE);
            Float[] rgb = colorRGBs.get(color_id);
            // FIXME: hack
            if (rgb == null)
                rgb = generate_next_color();
            for (Float f : rgb) System.out.print(f + ", ");
            System.out.println();
            float[] color = {rgb[0], rgb[1], rgb[2]};
//           float[] color = {0f, 0f, 0f};
            gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, color, 0);
           // gl.glColor3i(rgb[0].intValue(), rgb[1].intValue(), rgb[2].intValue());
            list_me_dots (assigned_colors.get(color_id), gl);
            gl.glEndList();

            dot_pointers.add(dots);
        }
        return dot_pointers;
    }

    private void list_me_dots (java.util.List<Tuple2<Position, FieldModel<IAtomContainer>>> dots, GL gl) {
        gl.glPushMatrix();
        gl.glTranslated((-length/2)+0.5, -width/2, 0);
        gl.glPointSize(5.0f);
        gl.glBegin(gl.GL_POINTS);

        for (Tuple2<Position, FieldModel<IAtomContainer>> model : dots) {
            int x = model._1().x();
            int y = model._1().y();
            gl.glVertex2i(x, y);
        }

        gl.glEnd();
        gl.glPopMatrix();
    }

    public Map color_me_dots() {
        Map<Integer, java.util.List<Tuple2<Position, FieldModel<IAtomContainer>>>>
                assigned_colors =
                new HashMap<Integer, java.util.List<Tuple2<Position, FieldModel<IAtomContainer>>>>
                (field.size());
        Map<String, Integer> modelColors = new HashMap<String, Integer>();

        int color_position = 0;

        for (Tuple2<Position, FieldModel<IAtomContainer>> m : field) {
            String id = m._2().getGeneralizeMedian().getID();
            if( !modelColors.containsKey(id) ) {
                Float[] new_color = generate_next_color();
//                color_id = Color.values()[color_position];
                colorRGBs.put(color_position, new_color);
                color_position++;
                assigned_colors.put(color_position, new LinkedList<Tuple2<Position, FieldModel<IAtomContainer>>>());
                modelColors.put(id, color_position);
            }

            int color_id = modelColors.get(id);
            assigned_colors.get(color_id).add(m);

        }

        return assigned_colors;
    }

    private Float[] generate_next_color () {
        float red = random_gen.nextFloat();// * random_gen.nextInt(235) + 20;
        float green = random_gen.nextFloat();// * random_gen.nextInt(235) + 20;
        float blue = random_gen.nextFloat();// * random_gen.nextInt(235) + 20;
//        float green = red, blue = red;

        Float[] color = {red, green, blue};
        return color;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }
}
}