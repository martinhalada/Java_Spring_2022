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


5. Describe Maven goals


6. How are project dependencies managed by Maven
