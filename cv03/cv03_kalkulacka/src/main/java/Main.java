import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        boolean konec = false;
        while(!konec) {
            System.out.println("Kalkulačka");
            System.out.print("Zadej 1. číslo: ");
            double a = sc.nextDouble();
            System.out.print("Zadej 2. číslo: ");
            double b = sc.nextDouble();
            System.out.print("Zadej operaci (+,-,*,/): ");
            char operace = sc.next().charAt(0);

            Kalkulacka kalkulacka = new Kalkulacka(a, b, operace);
            System.out.println(kalkulacka.vypocitej());

            sc.nextLine();
            System.out.print("Další příklad? (ano, ne)");
            String dalsi = sc.nextLine();
            if (dalsi.compareToIgnoreCase("ano")==1) konec = true;
        }
    }
}
