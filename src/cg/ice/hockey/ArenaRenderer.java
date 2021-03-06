package cg.ice.hockey;

import cg.ice.hockey.strategies.circle.CircleStrategy;
import cg.ice.hockey.strategies.line.LineStrategy;
import cg.ice.hockey.util.Circle;
import cg.ice.hockey.util.Line;
import cg.ice.hockey.util.Point;
import static com.jogamp.opengl.GL.GL_POINTS;
import com.jogamp.opengl.GL2;
import java.util.ArrayList;
import java.awt.Color;

public class ArenaRenderer {
    private GL2 gl;
    private int brushSize, globalBrushSize;
    private LineStrategy lineStrategy;
    private CircleStrategy circleStrategy;
    private boolean created = false, useGlobalBrushSize;

    public ArenaRenderer(GL2 gl, LineStrategy lineStrategy, CircleStrategy circleStrategy) {
        this.gl = gl;
        this.lineStrategy = lineStrategy;
        this.circleStrategy = circleStrategy;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public void setGlobalBrushSize(int globalBrushSize) {
        this.globalBrushSize = globalBrushSize;
        
        if (!created) {
            brushSize = globalBrushSize;
        }
    }

    public void setUseGlobalBrushSize(boolean useGlobalBrushSize) {
        this.useGlobalBrushSize = useGlobalBrushSize;
    }
    
    public void setLineStrategy(LineStrategy lineStrategy) {
        this.lineStrategy = lineStrategy;
    }

    public void setCircleStrategy(CircleStrategy circleStrategy) {
        this.circleStrategy = circleStrategy;
    }
    
    public void drawArena(Point p1, Point p2) {
        int width = p2.x - p1.x, height = p2.y - p1.y;
        
        int radiusCorner = (int) (width * 0.28), radiusZones = (int) (width * 0.14), radiusGoalZone = (int) (width * 0.09);
        int goalAreaHeight = (int) (height * 0.07), goalLineMargin = (int) (width * 0.065), midAreaHeight = (int) (height * 0.35), 
            centralLineHeight = height / 2;
        
        Circle tlCorner = circleStrategy.generateQuadrant(new Point(p1.x + radiusCorner, p1.y + radiusCorner), radiusCorner, 3);
        Circle trCorner = circleStrategy.generateQuadrant(new Point(p2.x - radiusCorner, p1.y + radiusCorner), radiusCorner, 4);
        Circle brCorner = circleStrategy.generateQuadrant(new Point(p2.x - radiusCorner, p2.y - radiusCorner), radiusCorner, 1);
        Circle blCorner = circleStrategy.generateQuadrant(new Point(p1.x + radiusCorner, p2.y - radiusCorner), radiusCorner, 2);
        
        Circle topGoalZone = circleStrategy.generateQuadrant(new Point (p1.x + width / 2, p1.y + goalAreaHeight), radiusGoalZone, new int[]{1, 2});
        Circle bottomGoalZone = circleStrategy.generateQuadrant(new Point (p1.x + width / 2, p2.y - goalAreaHeight), radiusGoalZone, new int[]{3, 4});
        Circle centerZone = circleStrategy.generateCircle(new Point (p1.x + width / 2, p1.y + centralLineHeight), (int) (width * 0.16));
        Circle tlZone = circleStrategy.generateCircle(new Point(p1.x + (int) (radiusCorner * 0.9), p1.y + (int) (radiusCorner * 1.15)), radiusZones);
        Circle trZone = circleStrategy.generateCircle(new Point(p2.x - (int) (radiusCorner * 0.9), p1.y + (int) (radiusCorner * 1.15)), radiusZones);
        Circle blZone = circleStrategy.generateCircle(new Point(p1.x + (int) (radiusCorner * 0.9), p2.y - (int) (radiusCorner * 1.15)), radiusZones);
        Circle brZone = circleStrategy.generateCircle(new Point(p2.x - (int) (radiusCorner * 0.9), p2.y - (int) (radiusCorner * 1.15)), radiusZones);
        
        ArrayList<Circle> circles = new ArrayList();
        circles.add(tlCorner);
        circles.add(trCorner);
        circles.add(brCorner);
        circles.add(blCorner);
        circles.add(topGoalZone);
        circles.add(bottomGoalZone);
        circles.add(centerZone);
        circles.add(tlZone);
        circles.add(trZone);
        circles.add(blZone);
        circles.add(brZone);
        
        Line topSide = lineStrategy.generateLine(new Point(p1.x + radiusCorner, p1.y), new Point(p2.x - radiusCorner, p1.y), brushSize);
        Line rightSide = lineStrategy.generateLine(new Point(p2.x, p1.y + radiusCorner), new Point(p2.x, p2.y - radiusCorner), brushSize);
        Line bottomSide = lineStrategy.generateLine(new Point(p1.x + radiusCorner, p2.y), new Point(p2.x - radiusCorner, p2.y), brushSize);
        Line leftSide = lineStrategy.generateLine(new Point(p1.x, p1.y + radiusCorner), new Point(p1.x, p2.y - radiusCorner), brushSize);
        
        Line topGoalLine = lineStrategy.generateLine(new Point(p1.x + goalLineMargin, p1.y + goalAreaHeight), new Point(p2.x - goalLineMargin, p1.y + goalAreaHeight), brushSize);
        Line bottomGoalLine = lineStrategy.generateLine(new Point(p1.x + goalLineMargin, p2.y - goalAreaHeight), new Point(p2.x - goalLineMargin, p2.y - goalAreaHeight), brushSize);
        
        Line topMidAreaLine = lineStrategy.generateLine(new Point(p1.x, p1.y + midAreaHeight), new Point(p2.x, p1.y + midAreaHeight), brushSize);
        Line centralMidAreaLine = lineStrategy.generateLine(new Point(p1.x, p1.y + centralLineHeight), new Point(p2.x, p1.y + centralLineHeight), brushSize);
        Line bottomMidAreaLine = lineStrategy.generateLine(new Point(p1.x, p2.y - midAreaHeight), new Point(p2.x, p2.y - midAreaHeight), brushSize);
        
        ArrayList<Line> lines = new ArrayList();
        lines.add(topSide);
        lines.add(rightSide);
        lines.add(bottomSide);
        lines.add(leftSide);
        lines.add(topGoalLine);
        lines.add(bottomGoalLine);
        lines.add(topMidAreaLine);
        lines.add(centralMidAreaLine);
        lines.add(bottomMidAreaLine);
        
        gl.glColor3ub((byte) 0, (byte) 0, (byte) 0);
        
        if (!useGlobalBrushSize) {
            gl.glPointSize(brushSize);
        } else {
            gl.glPointSize(globalBrushSize);
        }
        
        circles.forEach(circle -> {
            gl.glBegin(GL_POINTS);
                circle.points.forEach(p -> {
                    gl.glVertex2d(p.x, p.y);
                });
            gl.glEnd();
        });
        
        lines.forEach(line -> {
            line.points.forEach(p -> {
                gl.glBegin(GL_POINTS);
                    gl.glVertex2d(p.x, p.y);
                gl.glEnd();
            });
        });
        
        gl.glPointSize(1);
    }
}
