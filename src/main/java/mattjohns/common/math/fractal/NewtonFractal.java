package mattjohns.common.math.fractal;

import java.util.function.Function;

import mattjohns.common.math.ComplexNumber;
import mattjohns.common.math.General;

// From https://codereview.stackexchange.com/questions/140254/creating-a-newton-fractal-based-on-a-polynomial/140306 .
// Still needs the simple fixes suggested on that page, mainly automating the derivative rather than hardcoding. 
public class NewtonFractal {
    private double scale;
    
    private ComplexNumber h = new ComplexNumber(0.000001, 0.0);
    
    private Function<ComplexNumber, ComplexNumber> function;
    private Function<ComplexNumber, ComplexNumber> preDerivative;
    private Function<ComplexNumber, ComplexNumber> derivative;
    private ComplexNumber [] zeros;

    public NewtonFractal(double scale) {
        if (General.isNearlyZero(scale))
            this.scale = 1;
        else
            this.scale = scale;

        function = z -> z.cube().subtract(new ComplexNumber(1, 0));
        preDerivative = function.compose((ComplexNumber c) -> c.add(h));
        derivative = c -> preDerivative.apply(c).subtract(function.apply(c)).divide(h);
        
        zeros = new ComplexNumber[] {new ComplexNumber(1, 0),
                new ComplexNumber(-.5, Math.sqrt(3)/2),
                new ComplexNumber(-.5, -Math.sqrt(3)/2)};
    }
    
    // returns brightness between 0 and 0.96
    public double get(int x, int y) {
        int [] arr = applyNewtonMethod((double)x / scale, (double)y / scale);
        if (arr[0] == -1)
            return 0;
        
        double pixelReductionFactor = Math.pow(0.96, arr[1]);
        
        return pixelReductionFactor;
    }

    // https://codereview.stackexchange.com/questions/140254/creating-a-newton-fractal-based-on-a-polynomial/140306
    private int[] applyNewtonMethod(double x, double y) {
        ComplexNumber c = new ComplexNumber(x, y);
        double tolerance = 1E-6;
        int iterations = 1, max = 512;
        while (iterations < max) {
            c = c.subtract(function.apply(c).divide(derivative.apply(c)));
            for (int k = 0; k < zeros.length; k++) {
                ComplexNumber z = zeros[k], difference = c.subtract(z);
                if (Math.abs(difference.re()) < tolerance && Math.abs(difference.im()) < tolerance)
                    return new int[] {k, iterations};
            }
            iterations++;
        }
        return new int[] {-1};
    }
}
