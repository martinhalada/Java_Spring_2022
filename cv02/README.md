# Exam questions

1. Describe JVM heap and stack. Which variables are stored on heap and which on stack

    - JVM rozděluje paměť na stack (zásobník) a heap (haldu)
    - Stack se v Javě používá pro statickou alokaci paměti, práci s vlákny.
    - Stack obsahuje hodnoty primitivních datových typů, referencí na objekty, lokální proměnné, pořadí vykonávání metody
    - přístup do této paměti je pomocí LIFO, protože je přístup globální
    - jakmile se ukončí vykonávání metody, tak se ,,stack frame´´ vyprázdní
    - proměnné uvnitř zásovníku existují tak dlouho, dokud je spuštěna metoda, která je vytvořila
    - při zaplnění nastane výjimka java.lang.StackOverFlowError.
    - je threadsafe, protože každé vlákno má vlastní zásobník

    ---

    - Heap (halda) se využívá pro dynamickou alokaci paměti objektů a tříd za běhu
    - nové objekty jsou vždy vytvářeny v prostoru haldy a odkazy na tyto objekty jsou uloženy v zásobníku
    - tyto objekty mají globální přístup (lze k nim přistupovat odkudkoli z aplikace)
    - haldu lze rozdělit na menší části (generace) podle toho, jak dlouho jsou objekty alokovány - mladá, stará, trvalá
    - při zaplnění nastane výjimka java.lang.OutOfMemoryError.
    - přístup k haldě je pomalejší než do zásobníku
    - oproti zásobníku není automaticky dealokována - je proto potřeba využít Garbage Collector
    - oproti zásobníku není threadsafe a je třeba ji chránit správnou synchronizací kódu


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

    1. Stop the world
  
  
    2. GC roots

---

3. Describe G1 collector

---

4. Describe ZGC collector

---

5. Compare G1 vs ZGC

---

6. Describe bytecode (groups, prefix/suffix, operand types)

---

7. How is bytecode generated and how can be viewed

---

8. Describe operand stack and local variables array

---

9. Describe how does bytecode interpretation works in runtime

---

10. What is JIT compilation, how does it work

