package model.components;

import lombok.ToString;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

@ToString
public class Matrix
{
    private final double[][] values;

    public Matrix()
    {
        values = new double[3][3];
    }

    public Matrix(final double m11, final double m12, final double m13,
                  final double m21, final double m22, final double m23,
                  final double m31, final double m32, final double m33)
    {
        values = new double[][]
        {
            {m11, m12, m13},
            {m21, m22, m23},
            {m31, m32, m33}
        };
    }

    public Matrix(final double[][] matrix)
    {
        values = matrix;
    }

    public Matrix invert(){
        double determinant = getDeterminant(values);
        double[][] inverse = new double[3][3];

        if (determinant != 0)
        {
            inverse[0][0] = ((values[1][1]*values[2][2]) - (values[1][2]*values[2][1]));
            inverse[0][1] = -((values[0][1]*values[2][2]) - (values[0][2]*values[2][1]));
            inverse[0][2] = ((values[0][1]*values[1][2]) - (values[0][2]*values[1][1]));

            inverse[1][0] = -((values[1][0]*values[2][2]) - (values[1][2]*values[2][0]));
            inverse[1][1] = ((values[0][0]*values[2][2]) - (values[0][2]*values[2][0]));
            inverse[1][2] = -((values[0][0]*values[1][2]) - (values[0][2]*values[1][0]));

            inverse[2][0] = ((values[1][0]*values[2][1]) - (values[1][1]*values[2][0]));
            inverse[2][1] = -((values[0][0]*values[2][1]) - (values[0][1]*values[2][0]));
            inverse[2][2] = ((values[0][0]*values[1][1]) - (values[0][1]*values[1][0]));
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    inverse[i][j] *= 1/determinant;
                }
            }
        }
        return new Matrix(inverse);
    }

    public Matrix multiply(final Matrix other)
    {
        return new Matrix(
            ((values[0][0]*other.values[0][0]) + (values[0][1]*other.values[1][0]) + (values[0][2]*other.values[2][0])),
            ((values[0][0]*other.values[0][1]) + (values[0][1]*other.values[1][1]) + (values[0][2]*other.values[2][1])),
            ((values[0][0]*other.values[0][2]) + (values[0][1]*other.values[1][2]) + (values[0][2]*other.values[2][2])),

            ((values[1][0]*other.values[0][0]) + (values[1][1]*other.values[1][0]) + (values[1][2]*other.values[2][0])),
            ((values[1][0]*other.values[0][1]) + (values[1][1]*other.values[1][1]) + (values[1][2]*other.values[2][1])),
            ((values[1][0]*other.values[0][2]) + (values[1][1]*other.values[1][2]) + (values[1][2]*other.values[2][2])),

            ((values[2][0]*other.values[0][0]) + (values[2][1]*other.values[1][0]) + (values[2][2]*other.values[2][0])),
            ((values[2][0]*other.values[0][1]) + (values[2][1]*other.values[1][1]) + (values[2][2]*other.values[2][1])),
            ((values[2][0]*other.values[0][2]) + (values[2][1]*other.values[1][2]) + (values[2][2]*other.values[2][2]))
        );
    }

    public Point multiply(final Point point)
    {
        final double x = (point.x * values[0][0]) + (point.y * values[0][1]) + values[0][2];
        final double y = (point.y * values[1][0]) + (point.y * values[1][1]) + values[1][2];
        return new Point((int)x,(int)y);
    }

    public Rectangle multiply(final Rectangle rectangle)
    {
        final Point start = new Point(rectangle.x, rectangle.y);
        final Point end = new Point(rectangle.x + rectangle.width, rectangle.y + rectangle.height);
        final Point multiplyStart = multiply(start);
        final Point multiplyEnd = multiply(end);
        Rectangle retRectangle = new Rectangle(multiplyStart);
        retRectangle.add(multiplyEnd);
        return retRectangle;
    }

    public Point2D.Double multiply(final Point2D.Double point)
    {
        double destX = (values[0][0] * point.x) + (values[0][1] * point.y);
        double destY = (values[1][0] * point.x) + (values[1][1] * point.y);
        return new Point2D.Double(destX,destY);
    }

    public Polygon multiply(final Polygon poly)
    {
        Polygon retPoly = new Polygon();
        for (int i = 0; i < poly.npoints; i++)
        {
            Point pt = new Point(poly.xpoints[i], poly.ypoints[i]);
            pt = multiply(pt);
            retPoly.addPoint(pt.x, pt.y);
        }
        return retPoly;
    }

    public static Matrix translate(final double x, final double y)
    {
        return new Matrix(
                1, 0, x,
                0, 1, y,
                0, 0, 1);
    }

    public static Matrix translate(final Point point)
    {
        return new Matrix(
                1, 0, point.x,
                0, 1, point.y,
                0, 0, 1);
    }

    public static Matrix scale(final double scaleVal)
    {
        return new Matrix(
                scaleVal, 0, 0,
                0, scaleVal, 0,
                0, 0, 1);
    }


    public static Matrix mirrorX()
    {
        return new Matrix(
                1, 0, 0,
                0, -1, 0,
                0, 0, 1);
    }

    public static Matrix mirrorY(){
        return new Matrix(
                -1, 0, 0,
                0, 1, 0,
                0, 0, 1);
    }

    public static Matrix rotate(final double alpha){
        return new Matrix(
                Math.cos(alpha), -(Math.sin(alpha)),0,
                Math.sin(alpha), Math.cos(alpha), 0,
                0,0,1);
    }

    public static double getZoomFactorX(final Rectangle world, final Rectangle window)
    {
        return ((double)window.width / (double)world.width);
    }

    public static double getZoomFactorY(final Rectangle world, final Rectangle window)
    {
        return ((double)window.height / (double)world.height);
    }

    public static Matrix zoomToFit(final Rectangle world, final Rectangle window)
    {
        Matrix translateToOrigin = translate(-world.getCenterX(), -world.getCenterY());
        Matrix scale = scale(Math.min(getZoomFactorX(world, window), getZoomFactorY(world, window)));
        Matrix translateToPosition = translate(window.getCenterX(), window.getCenterY());
        return translateToPosition.multiply(mirrorX())
                .multiply(scale)
                .multiply(translateToOrigin);
    }

    public static Matrix zoomPoint(final Matrix old, final Point zoomPoint, final double zoomScale)
    {
        Matrix translateToOrigin = translate(-zoomPoint.x, -zoomPoint.y);
        Matrix translateToPoint = translate(zoomPoint.x, zoomPoint.y);
        return translateToPoint.multiply(scale(zoomScale))
                .multiply(translateToOrigin)
                .multiply(old);
    }

    /*
     ************************************ private helper methods ************************************
     */

    private double getDeterminant(double[][] matrix) {
        double det = 0;
        det += matrix[0][0]*((matrix[1][1]*matrix[2][2]) - (matrix[1][2]*matrix[2][1]));
        det -= matrix[0][1]*((matrix[1][0]*matrix[2][2]) - (matrix[1][2]*matrix[2][0]));
        det += matrix[0][2]*((matrix[1][0]*matrix[2][1]) - (matrix[1][1]*matrix[2][0]));
        return det;
    }
}
