public class Kalkulacka {
    private double a;
    private double b;
    private char operace;

    public Kalkulacka(double a, double b, char operace) {
        this.a = a;
        this.b = b;
        this.operace = operace;
    }

    public double vypocitej(){
        double vysledek = 0;
        switch (operace){
            case '+':
                vysledek = a + b;
                break;
            case '-':
                vysledek = a - b;
                break;
            case '*':
                vysledek = a * b;
                break;
            case '/':
                if (b != 0)
                    vysledek = a / b;
                else
                    throw new ArithmeticException("Nulou nelze dělit!");
                break;
            default:
                throw new IllegalArgumentException("Zadaná operace neexistuje: " + operace);
        }
        return vysledek;
    }

}
