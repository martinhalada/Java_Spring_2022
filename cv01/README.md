# Exam questions

1. Describe 5 new features in Java since version 7
    1. Lambda expressions

        - krátký blok kódu, který přebírá parametry a vrací hodnotu
        - je to podobné metodám, ale nemají název, lze implementovat přímo do těla metody

        ```
        parameter -> expression
        (parameter1, parameter2) -> expression
        (parameter1, parameter2) -> { code block }
        ```

    2. For-each metoda

        - metoda která slouží pro iteraci přes prvky kolekce

        ```
        names.forEach(name -> {
            System.out.println(name);
        });
        ```

    3. try-with-resources

        - příkaz, který se používá při používání zdrojů (např. práce se soubory)
        - zajišťuje uzavření zdroje po vykonání operace
        - implementuje AutoCloseable interface

        ```
        private static void printFile() throws IOException {
            try(FileInputStream input = new FileInputStream("file.txt")) {
                int data = input.read();
                while(data != -1){
                    System.out.print((char) data);
                    data = input.read();
                }
            }
        }
        ```

    4. Switch expressions

        - jsou to výrazy, které se používají ve switch konstrukci (switch statement)
        - syntaxe využívá operátor -> namísto dvojtečky a nekončí příkazem break, protože se ,, nepropadává´´ skrz ,,cases´´
        - kritéria mohou být oddělena čárkami

        ```
        var result = switch(month) {
            case JANUARY, JUNE, JULY -> 3;
            case FEBRUARY, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER -> 1;
            case MARCH, MAY, APRIL, AUGUST -> 2;
            default -> 0; 
        };
        ```

    5. Text blocks

        - textové bloky, ohraničené třemi dvojitými uvozovkami

        ```
        public String getBlockOfHtml() {
            return """
                    <html>

                        <body>
                            <span>example text</span>
                        </body>
                    </html>""";
        }
        ```
        
---

2. What is local type inference (Java 10)

    - definování proměnné pomocí klíčového slova var bez specifikace typu
    - kompilátor si typ odvodí z poskytnuté hodnoty
    
    ```
    var data = new ArrayList<>();
    ```

---

3. Describe Java records in (Java 15)

    - speciální třída, cílem bylo redukovat kód (gettry, settry,...)
    - využití pro uložení záznamů získaných z DB, CSV souboru,...
    - i v recordu lze vytvořit vlastní konstruktor, metody,...

    ```
    public record Vehicle(String brand, String licensePlate) {
        public Vehicle(String brand) {
            this(brand, null);
        }
    }
    ```

---

4. Describe Java sealed classes introduced (Java 17)

    - třídy, které definují to, jaké třídy mohou implementovat/dědit
    - slouží pro lepší kontrolu dědičnosti

    ```
    public abstract sealed class Vehicle permits Car, Truck {
        protected final String registrationNumber;

        public Vehicle(String registrationNumber) {
            this.registrationNumber = registrationNumber;
        }

        public String getRegistrationNumber() {
            return registrationNumber;
        }
    }
    ```

---

5. Difference between Java and JVM

    - Java je programovací jazyk
    - Platforma Java je multiplatformní
    - Je to název pro soubor programů, sloužících pro vývoj a spuštění programů napsaných v Javě
    - Mezi ty dílčí programy patří kompilátor (zdrojový kód -> bytekód), JRE (běhové prostředí Javy, doplňuje JVM), knihovny
    
    ---
    
    - Java Virtual machine (JVM) - je hlavní součást platformy Java, vykonává bajtkód aplikace
    - součástí je kompilátor (bajtkód -> nativní instrukce CPU (strojový kód))
    - virtualizace (virtuální stroj) - pro spuštění na lib. platformě
    - sice multiplatformní, ale každý OS musí mít svůj JVM


