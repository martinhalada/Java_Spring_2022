# Exam questions

1. Describe difference between namespace, module and service
  
    - Java nepoužívá namespace, který se vyskytuje třeba v C#. Místo toho je v Javě package.
    - Package je pojmenovaná kolekce tříd a podbalíčků (subpackages). Balíčky slouží k seskupování souvisejících tříd a definují jmenný prostor (namespace) pro třídy, které obsahují. (Takže Java packages je vlastně namespace).
    - Modul je v Javě mechanismus, které slouží pro sdružování balíčků do skupin. Java modul je zabalen jako modulární JAR soubor. Modul má svůj název, seznam závislostí (na kterém jiném modulu je závislý).
    - Service (služba) je specifická funkce poskytovaná knihovnou. V Javě je služba definována sadou rozhraní a tříd. Služba obsahuje rozhraní nebo abstraktní třídu, která definuje funkcionalitu poskytovanou službou.
  
2. Describe Maven POM

    - Maven POM (Project Object Model) je označení pro XML reprezentaci projektu Maven uloženou v souboru s názvem pom.xml, jsou v něm obsaženy informace o projektu včetně konfiguračních detailů.
    - nachází se v kořenovém adresáři každého projektu.
    - Obsahuje popis projektu, informace o verzi, konfikurační detaily, závislosti, pluginy včetně jejich konfigurací
    - Každý soubor pom.xml představuje jeden projekt (artefakt). Artefakt je jednoznačně identifikován skupinou (groupId), názvem (artifactId) a verzí (version).


3. What is Super POM

    - Super POM je výchozí Maven POM. Všechny soubory POM dědí ze Super POM.
    - Soubor super POM definuje všechny výchozí konfigurace. I ta nejjednodušší forma souboru POM tedy zdědí všechny konfigurace definované v souboru super POM.
    - Elementy v Super POM jsou: repositories, pluginRepositories, build, reporting a profiles

4. Describe Maven build livecycle
    
    - Maven je založen na konceptu životního cyklu. To znamená, že proces sestavení a distribuce konkrétního artefaktu (projektu) je jasně definován.
    - Existují tři vestavěné životní cykly: default, clean a site. Default se stará o nasazení projektu, clean se stará o čištění projektu (odstraňuje všechny soubory vygenerované předchozím buildem), site se stará o vytvoření webové stránky projektu.
    - Každý z těchto životních cyklů je definován jiným seznamem fází
    - default životní cyklus se například skládá z následujících fází: validate, compile, test, package, verify, install, deploy (celkem jich je 23), clean jich má jen 3 a site 4.


5. Describe Maven goals

    - Každá fáze je posloupnost cílů (goal), které jsou zodpovědné za specifické úkoly
    - Když spustíme fázi – všechny cíle vázané na tuto fázi jsou splněny v daném pořadí.

6. How are project dependencies managed by Maven

    - V Mavenu existují 2 typy závislostí: přímé a tranzitivní
    - Přímé závislosti jsou ty, které výslovně zahrneme do projektu.
    - Ty lze zahrnout pomocí tagů <dependency>
    - Na druhou stranu tranzitivní závislosti vyžadují přímé závislosti. Maven automaticky zahrnuje požadované tranzitivní závislosti do našeho projektu.
    - Můžeme vypsat všechny závislosti včetně tranzitivních závislostí v projektu pomocí příkazu: mvn dependency:tree
    - Rozsahy závislostí (scopes) pomáhají omezit tranzitivní závislost. Také upravují classpath pro různé úlohy. Maven má šest výchozích rozsahů závislostí (compile, provided, runtime, test, system, import).
