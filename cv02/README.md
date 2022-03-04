# Exam questions

1. Describe JVM heap and stack. Which variables are stored on heap and which on stack

    - JVM rozděluje paměť na stack (zásobník) a heap (haldu)

    - Stack se v Javě používá pro statickou alokaci paměti, práci s vlákny.
    - Stack obsahuje hodnoty primitivních datových typů, referencí na objekty
    - přístup do této paměti je pomocí LIFO.
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

