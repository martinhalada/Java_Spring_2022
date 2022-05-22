# Exam question

1. Describe how does Spring JDBC interact with relational database

    - JDBC je API pro práci s relačními databázemi
    - vrstva mezi jazykem Java (aplikací) a relační databází
      - zajišťuje výkonnou nízko úrovňovou komunikaci mezi aplikací a databází
      - z výsledků příslušného dotazu lze přímo získat pouze instance základních datových typů
      - přístup k příslušné databázi je zajišťován pomocí JDBC driveru
    - Spring JDBC je abstraktní vrstva nad JDBC ve Springu - usnadňuje vývoj aplikací 
    - podporovanými typy přístupů k databázi jsou: jdbcTemplates (klasický nízkoúrovňový typ, využíván v rámci všech ostatních typů přístupů), NamedParameterJdbcTemplate (obaluje předchozí typ, zadávání dotazů pomocí pojmenovaných parametrů namísto symbolu “?”), SimpleJdbcInsert and SimpleJdbcCall (zjednodušené dotazování pomocí metadat poskytovaných databází přes příslušný JDBC driver)

2. Describe Spring transactions and their properties

    - transakce jsou posloupnost jedné/více SQL operací se kterou se zachází jako s celkem
    - buťo se se provede celá nebo se neprovede vůbec
    - Pokud mají být dané operace provedeny jako transakce, anotuje se příslušná metoda pomocí @Transactional
    - Koncept transakcí lze popsat pomocí následujících čtyř klíčových vlastností, které se označují jako ACID
    - Atomicita - Transakce by měla být považována za jedinou operační jednotku, což znamená, že celá sekvence operací je buď úspěšná, nebo neúspěšná.
    - Konzistence - Transakce transformuje databázi z jednoho konzistentního stavu do druhého (po dokončení musí být db opět v konzistentním stavu
    - Izolace - Se stejnou sadou dat může být současně zpracováváno mnoho transakcí. Každá transakce by měla být izolována od ostatních, aby nedošlo k poškození dat.
    - Trvanlivost - Po dokončení transakce musí být výsledky této transakce trvalé a nesmí být z databáze vymazány v důsledku selhání systému.
