# Exam questions

1. Describe JVM heap and stack. Which variables are stored on heap and which on stack

    - JVM rozděluje paměť na stack (zásobník) a heap (haldu)
    - Stack se v Javě používá pro statickou alokaci paměti, práci s vlákny.
    - Stack obsahuje hodnoty primitivních datových typů, referencí na objekty, lokální proměnné, pořadí vykonávání metody
    - přístup do této paměti je pomocí LIFO, (přístup je globální)
    - jakmile se ukončí vykonávání metody, tak se ,,stack frame´´ vyprázdní
    - proměnné uvnitř zásovníku existují tak dlouho, dokud je spuštěna metoda, která je vytvořila
    - při zaplnění nastane výjimka java.lang.StackOverFlowError.
    - je threadsafe, protože každé vlákno má vlastní zásobník
    - stack je velikostně omezen na základě OS, a je menší než heap
    - stack je rychlejší
    - automatická alokace/dealokace při zavolání/navrácení z metody

    ---

    - Heap (halda) se využívá pro dynamickou alokaci paměti objektů a tříd za běhu
    - nové objekty jsou vždy vytvářeny v prostoru haldy a odkazy na tyto objekty jsou uloženy v zásobníku
    - tyto objekty mají globální přístup (lze k nim přistupovat odkudkoli z aplikace)
    - haldu lze rozdělit na menší části (generace) podle toho, jak dlouho jsou objekty alokovány - mladá, stará, trvalá
    - při zaplnění nastane výjimka java.lang.OutOfMemoryError.
    - přístup k haldě je pomalejší než do zásobníku
    - oproti zásobníku není automaticky dealokována - je proto potřeba využít Garbage Collector
    - oproti zásobníku není threadsafe a je třeba ji chránit správnou synchronizací kódu
    - heap je pomalejší


---

2. How does GC work

    - GC hlídá všechny objekty v haldě (heap) a odstraňuje nevyužívané.
    - GC vykonává 2 kroky = ,,Mark and Sweep´´
        - Mark - GC detekuje která část paměti se používá a která ne
        - Sweep - při tomto kroku se odstraní objekty nalezené v předchozím kroku
    - Výhody GC
        - Není potřeba se zabývat alokací a uvolňováním paměti
        - Můžeme zvolit efektivní GC algoritmus dle chování aplikace
        - Odstraňuje riziko vzniku tzv. memory leaků
    - Nevýhody GC
        - Bez kontroly nad alokací a uvolňováním paměti
        - Stop the world události
        - Zabírá část výpočetního výkonu
    - Různé implementace - sériový GC, paralelní GC, CMS, G1, ZGC

    1. Stop the world
        - jsou to události, JVM pozastaví všechny vlákna, aby se mohl spustit GC
        - Aplikace se prakticky zastaví a nevykonává užitečnou práci – problém s timeout
        - Aby mohly vlákna zaparkovat, musí dorazit do místa, odkud je to bezpečné (návrat z metody, konec cyklu) – safepoint
        - Problém pro realtime aplikace
        - Pro velkou haldu (100GB+) může trvat i desítky sekund - problém pro uživatele – timeout požadavků  
  
    2. GC roots
        - jsou speciální objekty GC
        - jsou to výchozí body, k nim jsou připojeny další objekty (dosažitelné), naopak ty, které nejsou připojeny k GC roots jsou nedosažitelné (je možné je poté odstranit GC).
        - GC shromažďuje jen ty objekty, které nejsou kořeny GC (GC roots) a nejsou přístupné pomocí odkazů z kořene GC

---

3. Describe G1 collector

    - Garbage-First (G1) collector je cílený na multiprocesorové stroje s velkou pamětí.
    - G1 se používá v aplikacích, které mohou pracovat souběžně s aplikačními vlákny, které nepotřebují větší haldu a které potřebují předvídatelné GC pauzy
    - flexibilní využití paměti
    - G1 provádí souběžnou fázi globálního označování pro určení životnosti objektů v celé haldě. Po dokončení fáze označení, G1 ví, které oblasti jsou většinou prázdné. Proto GC probíhá nejprve v těchto oblastech, obvykle je zde velké množství volného prostoru. To je důvod, proč se tato metoda nazývá Garbage-First. 
    - G1 není real-time collector
    - Princip: halda je rozdělena na sadu stějně velkých oblastí (asi 2000, 1-32 MB), každý má souvislý rozsah virtuální paměti, určitým sadám oblastí jsou přiřazeny stejné role (eden, survivor space, old generation)
    - Live objekty jsou zkopírovány/přesunuty do jedné nebo více oblastí, které přežily
    - oblasti mohou měnit svoji velikost podle potřeby
    - G1 vybere oblasti s nejnižší „životností“ (ty oblasti, které lze shromáždit nejrychleji).

    - Halda rodělena na mladou a starou generaci
    - zaměřuje se mladou generaci, kde je to nejefektivnější, částěčně se zaměřuje i na starou generaci
    - měkteré operace jsou vždy prováděny ve stop-the-world pauzách
    - G1 sleduje chování aplikace a predikuje 

---

4. Describe ZGC collector

    - ZGC je navržen tak, aby zkrátil stop-the-world fázi na co nejkratší jak jde.
    - Dosahuje toho tím, že doba těchto pauz se nezvyšuje s velikostí haldy.
    - hodí se na serverové aplikace, kde jsou velké haldy běžné.
    - ZGC provádí veškerou práci souběžně, bez zastavení provádění aplikačních vláken na dobu delší než 10 ms, díky čemuž je vhodný pro aplikace, které vyžadují nízkou latenci.
    - využívá ,,barevné´´ (colored) 64 bitové reference. To znamená, že ZGC používá některé referenční bity (bity metadat) k označení stavu objektu.
    - Podobně jako u G1, ZGC rozděluje haldu, ale zde oblasti haldy mohou mít různé velikosti.
    - https://www.baeldung.com/jvm-zgc-garbage-collector

---

5. Compare G1 vs ZGC
    
    - 

---

6. Describe bytecode (groups, prefix/suffix, operand types)

    - Bytekód je strojový kód pro JVM
    - instrukce má 1 byte + parametry
    - některé instrukce mají v názvu i typ (istore, astore, Istore, fstore)
    - podle povahy lze instrukce rozdělit do několika skupin:
        - načítání a uložení (aload_0, istore)
        - aritmetické a logicné (ladd, fcmpl)
        - konverze typů (i2b, d2i)
        - vytváření objektů a manipulace s nimi (new, putfield)
        - práce se zásobníkem (swap, dup2)
        - vyvolání metody a návrat (invokespecial, areturn)
    - Mnoho instrukcí má předpony/přípony odkazující na typy operandů, se kterými pracují:
        | **předpona/přípona** | **typ operandu** |
        |:-----------------:|:----------------:|
        | i                 | integer          |
        | l                 | long             |
        | s                 | short            |
        | b                 | byte             |
        | c                 | character        |
        | f                 | float            |
        | d                 | double           |
        | a                 | reference        |

---

7. How is bytecode generated and how can be viewed

    - Vytvoření bytekódu: program (Program.java) se pomocí javac (javac Program.java) zkompiluje na Program.class
    - Zobrazení bytekódu: javap -v Main.class

---

8. Describe operand stack and local variables array

    - https://www.artima.com/insidejvm/ed2/jvm8.html
    - podobně jako local variables, je operand stack organizovaný jako pole slov (array of worlds)
    - Ale na rozdíl od lokálních proměnných, ke kterým se přistupuje prostřednictvím indexů pole, se k zásobníku operandů přistupuje pomocí push a pop.
    - Pokud instrukce vloží hodnotu do zásobníku operandů, tak pozdější instrukce může použít pop a použít tuto hodnotu.
    - Virtuální stroj ukládá do zásobníku operandů stejné datové typy
    - Virtuální stroj Java používá zásobník operandů jako pracovní prostor. 
    - Mnoho instrukcí vybírá hodnoty ze zásobníku operandů, pracuje s nimi a ukládá výsledek

    ```
    iload_0    // push the int in local variable 0
    iload_1    // push the int in local variable 1
    iadd       // pop two ints, add them, push result
    istore_2   // pop int, store into local variable 2
    ```
    
    - Local variables array je ve framu zásobníku organizována jako pole slov indexované od nuly.
    - instrukce, které používají hodnoty z oblasti lokálních proměnných poskytují indexy do pole (indexované od nuly)
    - hodnoty typu int, float, reference a returnAddress zabírají jednu buňku pole.
    - hodnoty typu byte, short, char jsou převedeny na int před uložením
    - hodnoty typu long a double zabírají 2 buňky pole.
    

---

9. Describe how does bytecode interpretation works in runtime

    - pro převedení bytekódu na strojový kód JVM používá Java interpret za běhu
    - stejně tak, jako kompilátor převede zdrojový kód do bytekódu, tak stejně tak Java interpret převede bytekód do strojového kódu
    - Java nejprve hledá třídu s metodou main(), jakmile ji JVM najde, tak interpret spustí aplikaci zavoláním metody main()

    - Interpret převádí bytekód do strojového kódu řádek po řádku za běhu, bez změny pořadí sekvence
    - ve srovnání s kompilátorem, je vykonávání programu pomalejší
    - program běží do té doby dokud se nenarazí na chybu (u kompilátoru se chyby vypíšou až na konci kompilace)

---

10. What is JIT compilation, how does it work

    - JIT = Just-In-Time
    - označení pro speciální metodu překladu využívající různé techniky pro urychlení běhu programů přeložených do bajtkódu. 
    - Program, který je spuštěn a prováděn, je v době provádění přeložen přímo do nativního strojového kódu počítače, na kterém je prováděn, čímž dochází k urychlení jeho běhu. 
    - Negativem této techniky je prodleva, kterou JIT kompilátor (nikoli interpret) stráví překladem do nativního kódu, a proto se do nativního kódu často překládají jen mnohokrát (řádově 10 000×) volané úseky programu. 
    - Hlavním problémem JIT je, že má málo času na provedení své práce. Tyto nevýhody lze eliminovat použitím trvalé cache. 
    - Naopak výhodou je, že je možné lépe optimalizovat pro daný procesor a využít jeho rozšířených instrukcí.

    - V době, kdy má být vykonán blok kódu, přeloží JIT kompilátor některé či všechny jeho části do strojového kódu pro lepší výkon. 
    - Toto lze provést na soubor, na funkci nebo dokonce na libovolný fragment kódu. 
    - Překlad tedy může být proveden až za běhu, odtud název Just In Time – Právě v čas. 
    - Překlad může být uložen do souboru (disková cache) a pokud má být znovu použit později, není nutná opětovná kompilace.





