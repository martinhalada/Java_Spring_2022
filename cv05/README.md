# Exam questions

1. Describe annotations

    - @springbootapplication
        - anotace povoluje následující anotace (a vlastně je zároveň nahrazuje s výchozími atributy): @EnableAutoConfiguration (ta povoluje auto konfigurační mechanismus), @ComponentScan (povoluje hledání @Component v balíku ve kterém se nachází), @Configuration (povoluje zaregistrovat další beany nebo importovat configurační třídy)
    - @EnableAutoConfiguration
        - anotace povoluje Springu automaticky konfigurovat aplikační kontext. Automaticky vytvoří a registruje beany, které jsou buď v jar souborech v classpath a beany námi definované.
    - @PropertySource
        - Pro přiřazení konfiguračního souboru k danému profilu se v rámci konfigurace používá anotace @PropertySource, která se uvádí nad danou konfigurační třídou
    - @Profile
        - Profily jsou základní funkcí frameworku – umožňují nám mapovat naše beany do různých profilů – například dev, test a prod
    - @value
        - Tuto anotaci lze použít pro injektování hodnot do polí v beanech spravovaných Springem a lze ji použít na úrovni proměnné nebo parametru konstruktoru/metody.


2. Describe basic components of Logback logging system

    - Logger - vstup = slouží pro komunikaci s logovacím systémem, předává a filtruje zprávy na základě aktivní úrovně
    - Appender - výběr výstupu = zapisuje zprávy (Soubor, MySQL, socket)
    - Encoder - výstup = zapisuje zprávy na výstup
    - Layout (pattern) = formát výstupu
    
3. Describe 3 logging levels with examples when to use them
    
    - ERROR
        - Na rozdíl od úrovně protokolování FATAL neznamená chyba přerušení aplikace. Namísto toho jde pouze o nemožnost přístupu ke službě nebo souboru. Tato chyba ERROR ukazuje selhání něčeho důležitého ve vaší aplikaci. Tato úroveň protokolu se používá v případě, že závažný problém brání funkcím v aplikaci v efektivním fungování. Většinou bude aplikace pokračovat v běhu, ale nakonec bude třeba problém řešit.
    - WARN
        - Úroveň protokolu WARN se používá při zjištění neočekávaného problému aplikace. To znamená, že si nejste zcela jisti, zda se problém bude opakovat, nebo zůstane. V tomto okamžiku nemusíte zaznamenat žádné poškození aplikace. Tento problém obvykle představuje situaci, která zastaví běh konkrétních procesů. Přesto to neznamená, že došlo k poškození aplikace. Ve skutečnosti by měl kód nadále fungovat jako obvykle.
    - INFO
        - Zprávy INFO se podobají běžnému chování aplikací. Uvádějí, co se stalo. Například zda se určitá služba zastavila nebo spustila nebo zda jste něco přidali do databáze. Informace zaznamenané pomocí protokolu INFO mají obvykle informativní charakter a není nutné se jimi nutně zabývat.

4. Difference between SL4F and Logback

    - SLF4J je logovací fasáda JVM
    - Předává se konstantní String obsahující placeholder ya argumenty 
    - Stále musíme vyhodnotil argumenty, ale již se nemusí vytvořit řetězec
    
    - Logback je logovací framework pro aplikace v Javě. 
    - Je zamýšlen jako nástupce populárního projektu log4j. 
    - Je rozdělen do tří modulů: logback-core, logback-classic a logback-access. 
    - Modul logback-core vytváří základy pro ostatní dva moduly, modul logback-classic nativně implementuje rozhraní API SLF4J, takže lze snadno přepínat mezi logbackem a jinými logovacími rámci, a modul logback-access se integruje s kontejnery Servlet, jako jsou Tomcat a Jetty, a poskytuje funkce logování s přístupem přes HTTP.



5. Describe one of open source logging stack

    - ELK
        - ELK Stack je soubor tří open-source produktů - Elasticsearch, Logstash a Kibana. 
        - ELK stack poskytuje centralizovaný logging za účelem identifikace problémů se servery nebo aplikacemi. 
        - Umožňuje prohledávat všechny protokoly na jednom místě. 
        - Pomáhá také najít problémy na více serverech propojením logů v určitém časovém úseku.
        - Zkratka E znamená ElasticSearch: slouží k ukládání protokolů.
        - L znamená LogStash : používá se jak k odesílání, tak ke zpracování a ukládání protokolů.
        - K znamená Kibana: je vizualizační nástroj (webové rozhraní), který je hostován prostřednictvím Nginx nebo Apache.
    
    - Grafana + Loki
        - Grafana je multiplatformní open source analytická a interaktivní vizualizační webová aplikace. 
        - Po připojení k podporovaným zdrojům dat poskytuje grafy, diagramy a upozornění pro web. 
        - Je rozšiřitelná pomocí systému zásuvných modulů.
        - Koncoví uživatelé mohou vytvářet komplexní monitorovací panely pomocí interaktivních nástrojů pro tvorbu dotazů.
        - Grafana se dělí na front end a back end napsané v jazyce TypeScript, respektive Go.
        - Loki je horizontálně škálovatelný, vysoce dostupný systém agregace logů pro více uživatelů inspirovaný systémem Prometheus. 
        - Neindexuje obsah protokolů, ale spíše sadu štítků pro každý tok protokolů.


6. Describe following application containerization terms

    - Image
    - Repository
    - Container
