## 🌐 DESARROLLO DE SOFTWARE - PRIMER PARCIAL

### 🔍 OBJETIVO
La aplicación analiza la secuencia genética (ADN) de cada persona, identificando características que la diferencien como mutante o humana. Si la secuencia contiene más de una serie de cuatro letras idénticas consecutivas en direcciones horizontales, verticales o diagonales, se clasifica como ADN mutante.

### 🧬 1. FUNCIONALIDADES PRINCIPALES
El algoritmo verifica si un ADN es mutante buscando al menos dos secuencias de cuatro letras idénticas consecutivas. Primero, revisa si el ADN ya fue analizado y está almacenado. Si no, analiza filas, columnas y diagonales de la matriz de ADN en busca de secuencias mutantes. Si encuentra al menos dos, clasifica el ADN como mutante, guarda el resultado en la base de datos y lo devuelve.

El umbral se refiere a la cantidad mínima de secuencias mutantes necesarias para clasificar un ADN como mutante. Este valor, definido por MUTANT_THRESHOLD, está configurado en 2, lo que significa que, si se encuentran al menos dos secuencias consecutivas de cuatro letras idénticas (ya sea en filas, columnas o diagonales), el ADN se considera mutante. Este umbral optimiza el proceso, ya que el análisis puede detenerse tan pronto como se encuentran dos secuencias, evitando revisiones innecesarias.


### 🧬 2. API REST
La API REST proporciona el endpoint /mutant que verifica si una secuencia de ADN es mutante.

Solicitud:
Método: POST
Endpoint: /mutant
las respuestas que podemos obtener son :200 OK si es mutante y 403 Forbidden si no es mutante.


### 🧬 3. Persistencia de Datos y Estadísticas
Mediante el uso de la base de datos H2, se almacenan las secuencias de ADN (evitando duplicados) y se implementa el endpoint /stats para consultar estadísticas en formato JSON:

{
  "count_mutant_dna": 35,
  "count_human_dna": 100,
  "ratio": 0.35
}


🧪 Pruebas de Carga y de Integración
Se realizaron pruebas de carga con JMeter y tests automáticos en JUnit para asegurar la robustez del sistema.

### 💻 PROBANDO LOS ENDPOINTS
Desde Postman:

Endpoint /stats

Método: GET
URL:
Externo (Render): https://desarrollo-de-software-parcial-mutantes.onrender.com/mutantapi/api/v1/stats
Local: http://localhost:8080/mutantapi/api/v1/stats
Ejecución: Haz clic en SEND.
Endpoint /mutant

Método: POST
URL:
Externo (Render): https://desarrollo-de-software-parcial-mutantes.onrender.com/mutantapi/api/v1/mutant
Local: http://localhost:8080/mutantapi/api/v1/mutant
Cuerpo (Body): Selecciona RAW y usa el formato JSON.
JSONs de Prueba:


### 🧬 Ejemplos de secuencias JSON para /mutant:

#### Mutante en Filas: { "dna": ["CCCCTA", "TGCAGT", "GCTTCC", "CCCCTG", "GTAGTC", "AGTCAC"] }

#### Mutante en Diagonal: { "dna": ["GGTGTG", "TCGCCG", "CCAAAA", "ACTGAT", "GCCAGC", "CTACTA"] }

#### No mutante: { "dna": ["ATCGAT", "CTCTTG", "CAAGGC", "GGTATT", "ATCGAT", "AAGTCC"] }


### 📥 INSTALACIÓN DEL PROYECTO
Descarga el archivo ZIP, extráelo y configura las propiedades de la base de datos H2 en application.properties. Una vez listo, compila y ejecuta. La API estará activa en http://localhost:8080. Puedes probar el sistema usando herramientas como Postman para explorar los endpoints y analizar los resultados. 🎯