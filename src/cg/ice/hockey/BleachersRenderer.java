package cg.ice.hockey;

import cg.ice.hockey.strategies.circle.CircleStrategy;
import cg.ice.hockey.strategies.line.LineStrategy;
import cg.ice.hockey.util.Line;
import cg.ice.hockey.util.Point;
import static com.jogamp.opengl.GL.GL_POINTS;
import com.jogamp.opengl.GL2;
import java.awt.Color;
import java.util.ArrayList;

public class BleachersRenderer {
    private GL2 gl;
    private int brushSize;
    private Color brushColor;
    private LineStrategy lineStrategy;
    
    private ArrayList<Point[]> bleachers = new ArrayList();

    public BleachersRenderer(GL2 gl, int brushSize, Color brushColor, LineStrategy lineStrategy) {
        this.gl = gl;
        this.lineStrategy = lineStrategy;
    }  

    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
    }

    public void setBrushColor(Color brushColor) {
        this.brushColor = brushColor;
    }
    
    public void setLineStrategy(LineStrategy lineStrategy) {
        this.lineStrategy = lineStrategy;
    }
    
    public void addBleacher(Point p1, Point p2) {
        this.bleachers.add(new Point[] {p1, p2});
    }
    
    void drawBleachrs() {
        this.bleachers.forEach(bleach -> {
            Line line = lineStrategy.generateLine(bleach[0], bleach[1]);
            
            gl.glColor3ub((byte) brushColor.getRed(), (byte) brushColor.getGreen(), (byte) brushColor.getBlue());
            gl.glPointSize(brushSize);
            
            gl.glBegin(GL_POINTS);
                line.points.forEach(p -> {
                    gl.glVertex2d(p.x, p.y);
                });
            gl.glEnd();
            
            gl.glPointSize(1);
        });
    }
    
    void reset() {
        this.bleachers = new ArrayList();
    }
}
