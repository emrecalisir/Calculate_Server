package com.geag.jscience;

import org.jscience.mathematics.function.Polynomial;
import org.jscience.mathematics.function.Term;
import org.jscience.mathematics.function.Variable;
import org.jscience.mathematics.number.Float64;
import org.jscience.mathematics.number.Integer64;
import org.jscience.mathematics.vector.Float64Matrix;

public class JScienceCalculation {
	
	public String multiplyPolynomes(String s) {

		  Polynomial<Integer64> p1 = create(s.split(" "));
          System.out.println("Polynomial:  " + p1);
          Polynomial<Integer64> p2 = p1.times(p1);
          System.out.println("Polynomial:  " + p2);
          Polynomial<Integer64> p3 = p2.times(p2);
          System.out.println("Polynomial:  " + p3);
          Polynomial<Integer64> p4 = p3.times(p3);
          System.out.println("Polynomial:  " + p4);
          Polynomial<Integer64> p5 = p4.times(p4);
          System.out.println("Polynomial:  " + p5);
          return "";

	}

	  public static Polynomial<Integer64> create(String... a) {
	        Variable<Integer64> x = new Variable.Local<Integer64>("x");
	        Polynomial<Integer64> px = Polynomial.valueOf(Integer64.ZERO, x);
	        for (int i = 0, e = a.length - 1; i < a.length; i++, e--) {
	            px = px.plus(Polynomial.valueOf(
	                Integer64.valueOf(a[i]), Term.valueOf(x, e)));
	        }
	        return px;
	    }
	  
		public String multiplyMatrices(double[][] a, double[][] b) {

			Float64Matrix A = Float64Matrix.valueOf(a);
			Float64Matrix B = Float64Matrix.valueOf(b);
			Float64Matrix C = A.times(B);
			Float64Matrix D = C.transpose();
			Float64 determinant = D.determinant();
			return determinant.toString();
		}
}