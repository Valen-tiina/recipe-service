#  Recipe Service - Zabora

Sistema de gestión de recetas desarrollado con **Spring Boot** y **Spring Data JPA**. Proporciona endpoints RESTful para la creación, consulta y búsqueda de recetas con sus ingredientes, pasos de preparación, imágenes y metadatos asociados.

---

##  Tabla de Contenidos

- [Stack Tecnológico](#-stack-tecnológico)
- [Características Principales](#-características-principales)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Estructura de la Base de Datos](#-estructura-de-la-base-de-datos)
- [Endpoints de la API](#-endpoints-de-la-api)
  - [Recetas](#1-recetas)
  - [Ingredientes](#2-ingredientes)
  - [Catálogos](#3-catálogos-dificultades-categorías-sabores)
  - [Unidades y Medidas](#4-unidades-y-medidas)
- [Ejemplos de Uso](#-ejemplos-de-uso)
- [Estructura de Datos](#-estructura-de-datos)
- [Búsquedas y Filtros](#-búsquedas-y-filtros)
- [Testing](#-testing)
- [Contribuir](#-contribuir)
- [Licencia](#-licencia)

---

##  Stack Tecnológico

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 17+ | Lenguaje de programación |
| **Spring Boot** | 3.x | Framework principal |
| **Spring Data JPA** | 3.x | Persistencia de datos |
| **Hibernate** | 6.x | ORM |
| **MySQL** | 8.0+ | Base de datos |
| **Lombok** | - | Reducción de código boilerplate |
| **Swagger/OpenAPI** | 3.0 | Documentación de la API |

---

##  Características Principales

### 🍽 Gestión de Recetas
-  Creación completa de recetas con todos sus componentes
-  Consulta de recetas individuales o múltiples
-  Búsqueda por título
-  Búsqueda por ingredientes
-  Listado completo de recetas

###  Gestión de Ingredientes
-  CRUD completo de ingredientes
-  Asociación con sistemas de medidas
-  URLs de imágenes de ingredientes
-  Validación de unidades de medida

###  Catálogos Predefinidos
-  Niveles de dificultad (Baja, Media, Alta)
-  Categorías (Desayuno, Almuerzo, Cena, Snack)
-  Sabores (Salado, Dulce, Picante, Agridulce, Ácido)
-  Sistemas de medidas (Volumen, Masa)
-  Unidades de medida (ml, L, g, lb, etc.)

### 🖼 Gestión de Contenido Multimedia
-  Múltiples imágenes por receta
-  Imágenes en los pasos de preparación
-  Licencias para imágenes y recetas
-  Texto alternativo para accesibilidad

###  Pasos de Preparación
-  Orden secuencial de pasos
-  Tiempo estimado por paso
-  Descripción detallada
-  Imágenes ilustrativas opcionales

---

##  Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java JDK 17+** ([Descargar](https://www.oracle.com/java/technologies/downloads/))
- **MySQL 8.0+** ([Descargar](https://dev.mysql.com/downloads/mysql/))
- **Maven 3.6+** (incluido en la mayoría de IDEs)
- **Git** ([Descargar](https://git-scm.com/))
- **Postman** o **cURL** (opcional, para testing)

---

##  Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/Valen-tiina/recipe-service.git
cd recipe-service
```

### 2. Configurar la Base de Datos

#### 2.1. Crear la Base de Datos

El script de creación de la base de datos se encuentra en el archivo **`recipe_service.sql`** en la raíz del proyecto.

```bash
# Opción 1: Desde la terminal de MySQL
mysql -u root -p < recipe_service.sql

# Opción 2: Desde MySQL Workbench o phpMyAdmin
# Importar el archivo recipe_service.sql
```

Esto creará:
- La base de datos `recipe_service`
- Todas las tablas necesarias
- Datos predefinidos (dificultades, categorías, sabores, medidas, unidades)

#### 2.2. Datos Predefinidos Incluidos

**Dificultades:**
- Baja
- Media
- Alta

**Categorías:**
- Desayuno
- Almuerzo
- Cena
- Snack

**Sabores:**
- Salado
- Dulce
- Picante
- Agridulce
- Ácido

**Sistemas de Medida:**
- Volumen
- Masa

**Unidades:**
- Volumen: Mililitro, Litro, Cucharadita, Cucharada, Onza líquida, Taza
- Masa: Libra, Onza, Gramo

### 3. Configurar Credenciales de la Aplicación

Edita el archivo **`src/main/resources/application.yml`** (o `application.properties`) con tus credenciales de MySQL:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/recipe_service?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
    username: TU_USUARIO_MYSQL    # ← Cambiar esto
    password: TU_CONTRASEÑA_MYSQL # ← Cambiar esto
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: none  # Las tablas ya están creadas con el script SQL
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect

server:
  port: 8001 
```

** IMPORTANTE**: 
- Cambia `TU_USUARIO_MYSQL` por tu usuario de MySQL (normalmente `root`)
- Cambia `TU_CONTRASEÑA_MYSQL` por tu contraseña de MySQL
- Si ya tienes otro servicio en el puerto 8080, usa 8001 o cualquier otro disponible

### 4. Compilar y Ejecutar

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8001**

### 5. Verificar la Instalación

#### Opción 1: Con cURL
```bash
curl http://localhost:8001/difficulties
```

#### Opción 2: Con el navegador
Visita: http://localhost:8001/swagger-ui.html

---

## 🗄 Estructura de la Base de Datos

### Diagrama de Relaciones

```
┌─────────────┐     ┌──────────────────┐     ┌─────────────┐
│  recipes    │────<│ recipe_categories│>────│ categories  │
└─────────────┘     └──────────────────┘     └─────────────┘
       │            ┌──────────────────┐     ┌─────────────┐
       │────────────<│  recipe_flavors  │>────│  flavors    │
       │            └──────────────────┘     └─────────────┘
       │            ┌──────────────────┐     ┌─────────────┐
       │────────────<│ recipe_images    │>────│licenses_img │
       │            └──────────────────┘     └─────────────┘
       │            ┌────────────────────┐   ┌─────────────┐
       │────────────<│recipe_ingredients │>──│ ingredients │
       │            └────────────────────┘   └─────────────┘
       │                                              │
       │            ┌──────────────────┐             │
       │────────────<│     steps        │             │
       │            └──────────────────┘             │
       │                                              │
       │            ┌──────────────────┐     ┌──────────────┐
       ├───────────>│   difficulty     │     │  measurement │
       │            └──────────────────┘     └──────────────┘
       │                                              │
       │            ┌──────────────────┐             │
       └───────────>│ licenses_recipe  │             │
                    └──────────────────┘             │
                                                      │
                                             ┌──────────────┐
                                             │    units     │<──┘
                                             └──────────────┘
```

### Tablas Principales

#### `recipes` - Tabla central de recetas

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `ID` | INT | ID único de la receta |
| `title` | VARCHAR(100) | Título de la receta |
| `short_desc` | VARCHAR(255) | Descripción corta |
| `total_time_min` | INT | Tiempo total en minutos |
| `difficulty_id` | INT | FK a dificultad (1-3) |
| `servings` | INT | Número de porciones |
| `license_recipe_id` | INT | FK a licencia de la receta |

#### `ingredients` - Ingredientes disponibles

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `ID` | INT | ID único del ingrediente |
| `name_ing` | VARCHAR(100) | Nombre del ingrediente |
| `image_url` | VARCHAR(150) | URL de la imagen |
| `measurement_id` | INT | FK al sistema de medida |

#### `recipe_ingredients` - Ingredientes en cada receta

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `ID` | INT | ID único |
| `recipe_id` | INT | FK a receta |
| `ingredient_id` | INT | FK a ingrediente |
| `quantity` | DECIMAL(10,2) | Cantidad necesaria |
| `unit_id` | INT | FK a unidad de medida |

#### `steps` - Pasos de preparación

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `ID` | INT | ID único del paso |
| `recipe_id` | INT | FK a receta |
| `step_order` | INT | Orden del paso |
| `description_step` | VARCHAR(255) | Descripción del paso |
| `time_seconds` | INT | Tiempo estimado en segundos |
| `image_url` | VARCHAR(1000) | Imagen ilustrativa (opcional) |

#### `recipe_images` - Imágenes de la receta

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `ID` | INT | ID único |
| `recipe_id` | INT | FK a receta |
| `image_url` | VARCHAR(1000) | URL de la imagen |
| `alt_text` | VARCHAR(100) | Texto alternativo |
| `position` | INT | Posición/orden de la imagen |
| `license_id` | INT | FK a licencia de imagen |

### Tablas de Catálogo

#### `difficulty` - Niveles de dificultad

| ID | Nombre |
|----|--------|
| 1 | Baja |
| 2 | Media |
| 3 | Alta |

#### `categories` - Categorías de recetas

| ID | Nombre |
|----|---------|
| 1 | Desayuno |
| 2 | Almuerzo |
| 3 | Cena |
| 4 | Snack |

#### `flavors` - Sabores

| ID | Nombre |
|----|-----------|
| 1 | Salado |
| 2 | Dulce |
| 3 | Picante |
| 4 | Agridulce |
| 5 | Ácido |

#### `measurement` - Sistemas de medida

| ID | Nombre |
|----|--------|
| 1 | Volumen |
| 2 | Masa |

#### `units` - Unidades de medida

| ID | measurement_id | Nombre |
|----|----------------|---------|
| 1 | 1 (Volumen) | Mililitro |
| 2 | 1 (Volumen) | Litro |
| 3 | 1 (Volumen) | Cucharadita |
| 4 | 1 (Volumen) | Cucharada |
| 5 | 1 (Volumen) | Onza líquida |
| 6 | 1 (Volumen) | Taza |
| 7 | 2 (Masa) | Libra |
| 8 | 2 (Masa) | Onza |
| 9 | 2 (Masa) | Gramo |

---

## 📡 Endpoints de la API

### Base URL
```
http://localhost:8001
```

---

##  Recetas

### 1 Crear Receta

```http
POST /api/recipes
Content-Type: application/json
```

**Body Completo:**
```json
{
  "title": "Tacos al Pastor",
  "shortDescription": "Tacos mexicanos tradicionales con carne de cerdo marinada",
  "servings": 4,
  "difficultyId": 2,
  "licenseName": "Creative Commons BY-SA 4.0",
  "licenseUrl": "https://creativecommons.org/licenses/by-sa/4.0/",
  "categoryIds": [2, 3],
  "flavorIds": [1, 3],
  "ingredients": [
    {
      "ingredientId": 1,
      "quantity": 500,
      "unitId": 9
    },
    {
      "ingredientId": 2,
      "quantity": 2,
      "unitId": 4
    },
    {
      "ingredientId": 3,
      "quantity": 1,
      "unitId": 6
    }
  ],
  "images": [
    {
      "name": "Imagen principal de tacos",
      "imageUrl": "https://ejemplo.com/images/tacos-principal.jpg",
      "altText": "Plato de tacos al pastor servidos",
      "position": 1
    },
    {
      "name": "Vista lateral",
      "imageUrl": "https://ejemplo.com/images/tacos-lateral.jpg",
      "altText": "Vista lateral de los tacos",
      "position": 2
    }
  ],
  "steps": [
    {
      "stepOrder": 1,
      "description": "Marinar la carne con especias durante 2 horas",
      "timeSeconds": 7200,
      "imageUrl": "https://ejemplo.com/images/step1-marinado.jpg"
    },
    {
      "stepOrder": 2,
      "description": "Asar la carne en trompo o sartén hasta que esté dorada",
      "timeSeconds": 1800,
      "imageUrl": "https://ejemplo.com/images/step2-asado.jpg"
    },
    {
      "stepOrder": 3,
      "description": "Calentar las tortillas y servir con cebolla, cilantro y piña",
      "timeSeconds": 300,
      "imageUrl": "https://ejemplo.com/images/step3-servir.jpg"
    }
  ]
}
```

**Respuesta Exitosa:**
```json
{
  "id": 1,
  "title": "Tacos al Pastor",
  "shortDescription": "Tacos mexicanos tradicionales con carne de cerdo marinada",
  "servings": 4,
  "totalTimeMin": 152,
  "difficulty": "Media",
  "license": {
    "id": 1,
    "name": "Creative Commons BY-SA 4.0",
    "urlImage": "https://creativecommons.org/licenses/by-sa/4.0/"
  },
  "categories": [
    {
      "id": 2,
      "name": "Almuerzo"
    },
    {
      "id": 3,
      "name": "Cena"
    }
  ],
  "flavors": [
    {
      "id": 1,
      "name": "Salado"
    },
    {
      "id": 3,
      "name": "Picante"
    }
  ],
  "images": [
    {
      "id": 1,
      "imageUrl": "https://ejemplo.com/images/tacos-principal.jpg",
      "licenseName": "Imagen principal de tacos"
    },
    {
      "id": 2,
      "imageUrl": "https://ejemplo.com/images/tacos-lateral.jpg",
      "licenseName": "Vista lateral"
    }
  ],
  "ingredients": [
    {
      "id": 1,
      "name": "Carne de cerdo",
      "imageUrl": "https://ejemplo.com/ingredients/cerdo.jpg",
      "measurementName": "Masa"
    }
  ],
  "steps": [
    {
      "id": 1,
      "stepOrder": 1,
      "description": "Marinar la carne con especias durante 2 horas",
      "timeSeconds": 7200,
      "imageUrl": "https://ejemplo.com/images/step1-marinado.jpg"
    },
    {
      "id": 2,
      "stepOrder": 2,
      "description": "Asar la carne en trompo o sartén hasta que esté dorada",
      "timeSeconds": 1800,
      "imageUrl": "https://ejemplo.com/images/step2-asado.jpg"
    },
    {
      "id": 3,
      "stepOrder": 3,
      "description": "Calentar las tortillas y servir con cebolla, cilantro y piña",
      "timeSeconds": 300,
      "imageUrl": "https://ejemplo.com/images/step3-servir.jpg"
    }
  ]
}
```

---

### Obtener Todas las Recetas

```http
GET /api/recipes
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "title": "Tacos al Pastor",
    "shortDescription": "Tacos mexicanos tradicionales...",
    "servings": 4,
    "totalTimeMin": 152,
    "difficulty": "Media",
    ...
  },
  {
    "id": 2,
    "title": "Pancakes Americanos",
    ...
  }
]
```

---

### Obtener Receta por ID

```http
GET /api/recipes/{id}
```

**Ejemplo:**
```bash
GET /api/recipes/1
```

**Respuesta:** (Igual a la respuesta de crear receta)

---

### Obtener Múltiples Recetas por IDs

```http
GET /api/recipes/multiple?ids=1,2,3
```

**Parámetros de Query:**
- `ids`: Lista de IDs separados por comas

**Ejemplo:**
```bash
GET /api/recipes/multiple?ids=1,3,5
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "title": "Tacos al Pastor",
    ...
  },
  {
    "id": 3,
    "title": "Ensalada César",
    ...
  },
  {
    "id": 5,
    "title": "Brownies de Chocolate",
    ...
  }
]
```

**Nota:** Las recetas se devuelven en el mismo orden que los IDs solicitados.

---

### Buscar Recetas por Título

```http
GET /api/recipes/search?title={término}
```

**Ejemplo:**
```bash
GET /api/recipes/search?title=tacos
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "title": "Tacos al Pastor",
    ...
  },
  {
    "id": 15,
    "title": "Tacos de Pescado",
    ...
  }
]
```

**Características:**
- Búsqueda case-insensitive
- Búsqueda por coincidencia parcial
- Retorna error si no encuentra resultados

---

### Buscar Recetas por Ingrediente

```http
GET /api/recipes/search/ingredient?ingredient={nombre}
```

**Ejemplo:**
```bash
GET /api/recipes/search/ingredient?ingredient=pollo
```

**Respuesta:**
```json
[
  {
    "id": 4,
    "title": "Pollo a la Plancha",
    ...
  },
  {
    "id": 7,
    "title": "Sopa de Pollo",
    ...
  }
]
```

**Características:**
- Búsqueda case-insensitive
- Búsqueda por coincidencia parcial en el nombre del ingrediente
- Retorna error si no encuentra resultados

---

## 2 Ingredientes

### Crear Ingrediente

```http
POST /ingredients
Content-Type: application/json
```

**Body:**
```json
{
  "name": "Tomate",
  "imageUrl": "https://ejemplo.com/images/tomate.jpg",
  "measurementId": 2
}
```

**Respuesta:**
```json
{
  "id": 10,
  "name": "Tomate",
  "imageUrl": "https://ejemplo.com/images/tomate.jpg",
  "measurementName": "Masa"
}
```

---

### Obtener Todos los Ingredientes

```http
GET /ingredients
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "Carne de cerdo",
    "imageUrl": "https://ejemplo.com/ingredients/cerdo.jpg",
    "measurementName": "Masa"
  },
  {
    "id": 2,
    "name": "Cebolla",
    "imageUrl": "https://ejemplo.com/ingredients/cebolla.jpg",
    "measurementName": "Masa"
  }
]
```

---

### Obtener Ingrediente por ID

```http
GET /ingredients/{id}
```

**Ejemplo:**
```bash
GET /ingredients/1
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "Carne de cerdo",
  "imageUrl": "https://ejemplo.com/ingredients/cerdo.jpg",
  "measurementName": "Masa"
}
```

---

### Actualizar Ingrediente

```http
PUT /ingredients/{id}
Content-Type: application/json
```

**Body:**
```json
{
  "name": "Carne de cerdo orgánica",
  "imageUrl": "https://ejemplo.com/ingredients/cerdo-organico.jpg",
  "measurementId": 2
}
```

**Respuesta:**
```json
{
  "id": 1,
  "name": "Carne de cerdo orgánica",
  "imageUrl": "https://ejemplo.com/ingredients/cerdo-organico.jpg",
  "measurementName": "Masa"
}
```

---

### Eliminar Ingrediente

```http
DELETE /ingredients/{id}
```

**Respuesta:**
```
204 No Content
```

---

## 3️⃣ Catálogos (Dificultades, Categorías, Sabores)

### Obtener Todas las Dificultades

```http
GET /difficulties
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "Baja"
  },
  {
    "id": 2,
    "name": "Media"
  },
  {
    "id": 3,
    "name": "Alta"
  }
]
```

---

### Obtener Todas las Categorías

```http
GET /categories
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "Desayuno"
  },
  {
    "id": 2,
    "name": "Almuerzo"
  },
  {
    "id": 3,
    "name": "Cena"
  },
  {
    "id": 4,
    "name": "Snack"
  }
]
```

---

### Obtener Todos los Sabores

```http
GET /flavors
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "Salado"
  },
  {
    "id": 2,
    "name": "Dulce"
  },
  {
    "id": 3,
    "name": "Picante"
  },
  {
    "id": 4,
    "name": "Agridulce"
  },
  {
    "id": 5,
    "name": "Ácido"
  }
]
```

---

## 4️⃣ Unidades y Medidas

### Obtener Todas las Unidades

```http
GET /units
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "Mililitro",
    "measurementName": "Volumen"
  },
  {
    "id": 2,
    "name": "Litro",
    "measurementName": "Volumen"
  },
  {
    "id": 9,
    "name": "Gramo",
    "measurementName": "Masa"
  }
]
```

---

### Obtener Todos los Sistemas de Medida

```http
GET /measurement
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "name": "Volumen"
  },
  {
    "id": 2,
    "name": "Masa"
  }
]
```

---

## 💡 Ejemplos de Uso

### Flujo Completo: Crear una Receta de Brownies

#### 1. Consultar Catálogos Necesarios

```bash
# Obtener dificultades
curl http://localhost:8001/difficulties

# Obtener categorías
curl http://localhost:8001/categories

# Obtener sabores
curl http://localhost:8001/flavors

# Obtener ingredientes disponibles
curl http://localhost:8001/ingredients

# Obtener unidades
curl http://localhost:8001/units
```

#### 2. Crear Ingredientes Faltantes (si es necesario)

```bash
curl -X POST http://localhost:8001/ingredients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Chocolate oscuro",
    "imageUrl": "https://ejemplo.com/chocolate.jpg",
    "measurementId": 2
  }'
```

#### 3. Crear la Receta

```bash
curl -X POST http://localhost:8001/api/recipes \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Brownies de Chocolate",
    "shortDescription": "Deliciosos brownies húmedos y chocolatosos",
    "servings": 8,
    "difficultyId": 1,
    "licenseName": "Receta Original Zabora",
    "licenseUrl": "https://zabora.com/licenses",
    "categoryIds": [4],
    "flavorIds": [2],
    "ingredients": [
      {
        "ingredientId": 10,
        "quantity": 200,
        "unitId": 9
      },
      {
        "ingredientId": 15,
        "quantity": 3,
        "unitId": 8
      }
    ],
    "images": [
      {
        "name": "Brownies terminados",
        "imageUrl": "https://ejemplo.com/brownies.jpg",
        "altText": "Plato de brownies recién horneados",
        "position": 1
      }
    ],
    "steps": [
      {
        "stepOrder": 1,
        "description": "Derretir el chocolate a baño maría",
        "timeSeconds": 300,
        "imageUrl": "https://ejemplo.com/step1.jpg"
      },
      {
        "stepOrder": 2,
        "description": "Mezclar los ingredientes secos",
        "timeSeconds": 180,
        "imageUrl": ""
      },
      {
        "stepOrder": 3,
        "description": "Hornear a 180°C por 25 minutos",
        "timeSeconds": 1500,
        "imageUrl": "https://ejemplo.com/step3.jpg"
      }
    ]
  }'
```

#### 4. Buscar Recetas Creadas

```bash
# Buscar por título
curl "http://localhost:8001/api/recipes/search?title=brownies"

# Buscar por ingrediente
curl "http://localhost:8001/api/recipes/search/ingredient?ingredient=chocolate"
```

---

##  Estructura de Datos

### CreateRecipe DTO (Request)

```json
{
  "title": "string (requerido)",
  "shortDescription": "string (requerido)",
  "servings": "integer (requerido)",
  "difficultyId": "integer (requerido, 1-3)",
  "licenseName": "string (requerido)",
  "licenseUrl": "string (requerido)",
  "categoryIds": ["array de integers (requerido)"],
  "flavorIds": ["array de integers (requerido)"],
  "ingredients": [
    {
      "ingredientId": "integer (requerido)",
      "quantity": "decimal (requerido)",
      "unitId": "integer (requerido)"
    }
  ],
  "images": [
    {
      "name": "string (requerido)",
      "imageUrl": "string (requerido)",
      "altText": "string (opcional)",
      "position": "integer (requerido)"
    }
  ],
  "steps": [
    {
      "stepOrder": "integer (requerido)",
      "description": "string (requerido)",
      "timeSeconds": "integer (requerido)",
      "imageUrl": "string (opcional)"
    }
  ]
}
```

### ResponseRecipes DTO (Response)

```json
{
  "id": "integer",
  "title": "string",
  "shortDescription": "string",
  "servings": "integer",
  "totalTimeMin": "integer (calculado automáticamente)",
  "difficulty": "string",
  "license": {
    "id": "integer",
    "name": "string",
    "urlImage": "string"
  },
  "categories": [
    {
      "id": "integer",
      "name": "string"
    }
  ],
  "flavors": [
    {
      "id": "integer",
      "name": "string"
    }
  ],
  "images": [
    {
      "id": "integer",
      "imageUrl": "string",
      "licenseName": "string"
    }
  ],
  "ingredients": [
    {
      "id": "integer",
      "name": "string",
      "imageUrl": "string",
      "measurementName": "string"
    }
  ],
  "steps": [
    {
      "id": "integer",
      "stepOrder": "integer",
      "description": "string",
      "timeSeconds": "integer",
      "imageUrl": "string"
    }
  ]
}
```

---

##  Búsquedas y Filtros

### Búsqueda por Título

**Características:**
- Case-insensitive (no distingue mayúsculas/minúsculas)
- Búsqueda parcial (encuentra coincidencias dentro del título)
- Retorna todas las recetas que contengan el término buscado

**Ejemplo:**
```bash
# Encuentra: "Tacos al Pastor", "Tacos de Pescado", "Mini Tacos"
GET /api/recipes/search?title=tacos
```

### Búsqueda por Ingrediente

**Características:**
- Case-insensitive
- Búsqueda parcial en el nombre del ingrediente
- Retorna todas las recetas que contengan ese ingrediente

**Ejemplo:**
```bash
# Encuentra recetas con: "Pollo", "Pechuga de pollo", "Caldo de pollo"
GET /api/recipes/search/ingredient?ingredient=pollo
```

### Obtener Recetas Específicas

**Uso:** Útil para crear menús o listas personalizadas

```bash
# Obtener las recetas 1, 5 y 10
GET /api/recipes/multiple?ids=1,5,10
```

---

##  Testing

### Testing Manual con Postman

**Colección sugerida de pruebas:**

1. **Obtener Catálogos**
   - GET /difficulties
   - GET /categories
   - GET /flavors
   - GET /units
   - GET /measurement

2. **Gestión de Ingredientes**
   - POST /ingredients (crear)
   - GET /ingredients (listar)
   - GET /ingredients/{id} (obtener uno)
   - PUT /ingredients/{id} (actualizar)
   - DELETE /ingredients/{id} (eliminar)

3. **Gestión de Recetas**
   - POST /api/recipes (crear)
   - GET /api/recipes (listar todas)
   - GET /api/recipes/{id} (obtener una)
   - GET /api/recipes/multiple?ids=1,2,3 (obtener varias)
   - GET /api/recipes/search?title=tacos (buscar por título)
   - GET /api/recipes/search/ingredient?ingredient=pollo (buscar por ingrediente)

### Testing con cURL

#### Ejemplo: Crear y Buscar una Receta

```bash
# 1. Crear ingrediente
INGREDIENT_RESPONSE=$(curl -s -X POST http://localhost:8001/ingredients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pasta",
    "imageUrl": "https://ejemplo.com/pasta.jpg",
    "measurementId": 2
  }')

INGREDIENT_ID=$(echo $INGREDIENT_RESPONSE | jq -r '.id')

# 2. Crear receta
RECIPE_RESPONSE=$(curl -s -X POST http://localhost:8001/api/recipes \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Pasta Alfredo",
    "shortDescription": "Pasta cremosa con salsa alfredo",
    "servings": 2,
    "difficultyId": 1,
    "licenseName": "Receta Original",
    "licenseUrl": "https://example.com",
    "categoryIds": [2],
    "flavorIds": [1],
    "ingredients": [
      {
        "ingredientId": '$INGREDIENT_ID',
        "quantity": 200,
        "unitId": 9
      }
    ],
    "images": [
      {
        "name": "Pasta lista",
        "imageUrl": "https://ejemplo.com/pasta-alfredo.jpg",
        "altText": "Plato de pasta alfredo",
        "position": 1
      }
    ],
    "steps": [
      {
        "stepOrder": 1,
        "description": "Cocinar la pasta al dente",
        "timeSeconds": 600,
        "imageUrl": ""
      }
    ]
  }')

RECIPE_ID=$(echo $RECIPE_RESPONSE | jq -r '.id')

# 3. Buscar la receta creada
curl "http://localhost:8001/api/recipes/search?title=pasta"
```

### Testing Automatizado

```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests de integración
mvn verify
```

---

##  Validaciones y Restricciones

### Campos Requeridos en Recetas
- `title`: No puede estar vacío
- `shortDescription`: No puede estar vacío
- `servings`: Debe ser mayor que 0
- `difficultyId`: Debe existir (1, 2 o 3)
- `categoryIds`: Debe tener al menos una categoría
- `flavorIds`: Debe tener al menos un sabor
- `ingredients`: Debe tener al menos un ingrediente
- `steps`: Debe tener al menos un paso

### Campos Requeridos en Ingredientes
- `ingredientId`: Debe existir en la BD
- `quantity`: Debe ser mayor que 0
- `unitId`: Debe existir y coincidir con el measurement del ingrediente

### Campos Requeridos en Pasos
- `stepOrder`: Debe ser único y secuencial
- `description`: No puede estar vacío
- `timeSeconds`: Debe ser mayor o igual a 0

### Límites de Longitud
- `title`: Máximo 100 caracteres
- `short_desc`: Máximo 255 caracteres
- `description_step`: Máximo 255 caracteres
- `image_url`: Máximo 1000 caracteres
- `name_ing`: Máximo 100 caracteres

---

##  Problemas Comunes

### Error: "Dificultad no encontrada"
**Causa**: El `difficultyId` no existe (debe ser 1, 2 o 3)  
**Solución**: Verificar que el ID sea válido consultando `/difficulties`

### Error: "Ingrediente no encontrado"
**Causa**: El `ingredientId` en la lista de ingredientes no existe  
**Solución**: Verificar IDs disponibles con `GET /ingredients` o crear el ingrediente primero

### Error: "Unidad no encontrada"
**Causa**: El `unitId` no existe o no coincide con el tipo de medida del ingrediente  
**Solución**: Consultar unidades disponibles con `GET /units`

### Error: "La medida con ID X no existe"
**Causa**: El `measurementId` al crear ingrediente no es válido (debe ser 1 o 2)  
**Solución**: Usar 1 para Volumen o 2 para Masa

### Error: Receta se crea pero `totalTimeMin` es 0
**Causa**: Los pasos no tienen `timeSeconds` definido  
**Solución**: Asegurarse de que cada paso tenga un valor válido en `timeSeconds`

---

##  Documentación Adicional

### Swagger UI
Accede a la documentación interactiva de la API:
```
http://localhost:8001/swagger-ui.html
```

### Relaciones Entre Entidades

**Una Receta puede tener:**
-  **Múltiples categorías** (N:M) - Ej: Desayuno + Snack
-  **Múltiples sabores** (N:M) - Ej: Dulce + Ácido
-  **Múltiples ingredientes** (N:M con cantidad y unidad)
-  **Múltiples imágenes** (1:N)
-  **Múltiples pasos** (1:N ordenados secuencialmente)
-  **Una dificultad** (N:1)
-  **Una licencia de receta** (N:1)

**Un Ingrediente puede:**
-  Usarse en **múltiples recetas** (N:M)
-  Tener **un sistema de medida** (Volumen o Masa)
-  Medirse con **múltiples unidades** del mismo sistema

---

##  Casos de Uso Típicos

### 1. Crear un Menú Diario

```bash
# Obtener recetas de desayuno
curl "http://localhost:8001/api/recipes" | jq '.[] | select(.categories[].name == "Desayuno")'

# Obtener recetas de almuerzo
curl "http://localhost:8001/api/recipes" | jq '.[] | select(.categories[].name == "Almuerzo")'

# Obtener recetas de cena
curl "http://localhost:8001/api/recipes" | jq '.[] | select(.categories[].name == "Cena")'
```

### 2. Filtrar por Dificultad

```bash
# Recetas fáciles para principiantes
curl "http://localhost:8001/api/recipes" | jq '.[] | select(.difficulty == "Baja")'
```

### 3. Buscar Recetas Vegetarianas

```bash
# Buscar recetas que NO contengan carne
curl "http://localhost:8001/api/recipes/search/ingredient?ingredient=vegetales"
```

### 4. Planificar Tiempo de Cocina

```bash
# Obtener recetas de menos de 30 minutos
curl "http://localhost:8001/api/recipes" | jq '.[] | select(.totalTimeMin <= 30)'
```

---

## 🤝 Contribuir

Las contribuciones son bienvenidas. Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

##  Notas Importantes

- El tiempo total de la receta (`totalTimeMin`) se calcula automáticamente sumando los `timeSeconds` de todos los pasos
- Las categorías y sabores son relaciones N:M, permitiendo múltiples valores
- Los pasos deben estar ordenados secuencialmente por `stepOrder`
- Las imágenes tienen un campo `position` para controlar su orden de visualización
- Cada ingrediente en una receta tiene su propia cantidad y unidad de medida
- Las licencias se guardan tanto para recetas como para imágenes

---

##  Integración con Auth Service

Este servicio está diseñado para funcionar en conjunto con el **Auth Service**. En el futuro, se implementará:

- Asociación de recetas con usuarios creadores
- Sistema de favoritos por usuario
- Recetas privadas vs públicas
- Comentarios y valoraciones
- Historial de recetas preparadas

---

##  Contacto

Para preguntas o soporte:
- Email: zabora.oficial@gmail.com
- Issues: [GitHub Issues](https://github.com/tu-usuario/recipe-service/issues)

---

##  Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

---

##  Roadmap

- [ ] Implementar actualización de recetas (PUT/PATCH)
- [ ] Implementar eliminación de recetas
- [ ] Sistema de valoraciones y comentarios
- [ ] Filtros avanzados (por tiempo, dificultad, ingredientes)
- [ ] Conversión automática de unidades
- [ ] Cálculo nutricional
- [ ] Sugerencias de recetas basadas en ingredientes disponibles
- [ ] Integración con Auth Service para usuarios
- [ ] Sistema de favoritos
- [ ] Exportar recetas a PDF
- [ ] API de recomendaciones personalizadas

---

<div align="center">

**Desarrollado por el equipo de Zabora**

 Si este proyecto te fue útil, considera darle una estrella en GitHub 

</div>