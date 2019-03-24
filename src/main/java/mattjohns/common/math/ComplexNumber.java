package mattjohns.common.math;

import java.util.Objects;

public class ComplexNumber {
	private final double real;
	private final double imaginary;

	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}

	public String toString() {
		if (imaginary == 0)
			return real + "";
		if (real == 0)
			return imaginary + "i";
		if (imaginary < 0)
			return real + " - " + (-imaginary) + "i";
		return real + " + " + imaginary + "i";
	}

	public double absolute() {
		return Math.hypot(real, imaginary);
	}

	public double phase() {
		return Math.atan2(imaginary, real);
	}

	public ComplexNumber add(ComplexNumber b) {
		ComplexNumber a = this;
		double real = a.real + b.real;
		double imag = a.imaginary + b.imaginary;
		return new ComplexNumber(real, imag);
	}

	public ComplexNumber subtract(ComplexNumber b) {
		ComplexNumber a = this;
		double real = a.real - b.real;
		double imag = a.imaginary - b.imaginary;
		return new ComplexNumber(real, imag);
	}

	public ComplexNumber multiply(ComplexNumber b) {
		ComplexNumber a = this;
		double real = a.real * b.real - a.imaginary * b.imaginary;
		double imag = a.real * b.imaginary + a.imaginary * b.real;
		return new ComplexNumber(real, imag);
	}

	public ComplexNumber scale(double alpha) {
		return new ComplexNumber(alpha * real, alpha * imaginary);
	}

	public ComplexNumber conjugate() {
		return new ComplexNumber(real, -imaginary);
	}

	public ComplexNumber reciprocal() {
		double scale = real * real + imaginary * imaginary;
		return new ComplexNumber(real / scale, -imaginary / scale);
	}

	public double re() {
		return real;
	}

	public double im() {
		return imaginary;
	}

	public ComplexNumber divide(ComplexNumber b) {
		ComplexNumber a = this;
		return a.multiply(b.reciprocal());
	}

	public ComplexNumber exp() {
		return new ComplexNumber(Math.exp(real) * Math.cos(imaginary), Math.exp(real) * Math.sin(imaginary));
	}

	public ComplexNumber sin() {
		return new ComplexNumber(Math.sin(real) * Math.cosh(imaginary), Math.cos(real) * Math.sinh(imaginary));
	}

	public ComplexNumber cos() {
		return new ComplexNumber(Math.cos(real) * Math.cosh(imaginary), -Math.sin(real) * Math.sinh(imaginary));
	}

	public ComplexNumber tan() {
		return sin().divide(cos());
	}

	public static ComplexNumber add(ComplexNumber a, ComplexNumber b) {
		double real = a.real + b.real;
		double imag = a.imaginary + b.imaginary;
		ComplexNumber sum = new ComplexNumber(real, imag);
		return sum;
	}

	public boolean equals(Object x) {
		if (x == null)
			return false;
		if (this.getClass() != x.getClass())
			return false;
		ComplexNumber that = (ComplexNumber)x;
		return (this.real == that.real) && (this.imaginary == that.imaginary);
	}

	public int hashCode() {
		return Objects.hash(real, imaginary);
	}

	public ComplexNumber square() {
		return this.multiply(this);
	}

	public ComplexNumber cube() {
		return this.multiply(this).multiply(this);
	}
}