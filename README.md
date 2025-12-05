#  Recipe Service – API REST

Servicio backend para la gestión de recetas, ingredientes, imágenes, pasos, categorías y sabores.
Este archivo documenta los **endpoints disponibles actualmente para Recetas e Ingredientes**.

---

#  INGREDIENTES

##  **1. Crear ingrediente**

**POST** `/ingredients`

```json
{
  "name": "Pollo",
  "measurementId": 1,
  "imageUrl": "https://..."
}
```

**Respuesta:**
201 CREATED + objeto creado

---

##  **2. Obtener todos los ingredientes**

**GET** `/ingredients`
**Respuesta:**
200 OK + lista de ingredientes

---

##  **3. Obtener ingrediente por ID**

**GET** `/ingredients/{id}`
**Respuesta:**
200 OK
404 si no existe

---

##  **4. Actualizar ingrediente**

**PUT** `/ingredients/{id}`

```json
{
  "name": "Pollo entero",
  "measurementId": 2,
  "imageUrl": "https://..."
}
```

**Respuesta:**
200 OK
404 si no existe

---

##  **5. Eliminar ingrediente**

**DELETE** `/ingredients/{id}`
**Respuesta:**
204 NO CONTENT
404 si no existe

---

#  RECETAS

## ➤ **1. Crear receta**

**POST** `/recipes`

```json
{
  "title": "Pollo al horno",
  "shortDescription": "Receta deliciosa y sencilla",
  "servings": 4,
  "difficultyId": 1,

  "licenseName": "Creative Commons",
  "licenseUrl": "https://example.com/license.png",

  "categoryIds": [1, 2],
  "flavorIds": [3],

  "ingredients": [
    {
      "ingredientId": 1,
      "unitId": 2,
      "quantity": 300
    }
  ],

  "images": [
    {
      "name": "Foto principal",
      "imageUrl": "https://res.cloudinary.com/xxx/image/upload/abc.png",
      "altText": "Pollo al horno servido",
      "position": 1
    },
    {
      "name": "Paso 1",
      "imageUrl": "https://res.cloudinary.com/xxx/image/upload/def.png",
      "altText": "Preparación inicial",
      "position": 2
    }
  ],

  "steps": [
    {
      "stepOrder": 1,
      "description": "Precalienta el horno a 180°C.",
      "timeSeconds": 120,
      "imageUrl": "https://res.cloudinary.com/xxx/image/upload/precalentar.png"
    },
    {
      "stepOrder": 2,
      "description": "Sazona el pollo y colócalo en la bandeja.",
      "timeSeconds": 300,
      "imageUrl": "https://res.cloudinary.com/xxx/image/upload/sazonar.png"
    }
  ]
}

```

**Respuesta:**
201 CREATED + receta creada

---

## ➤ **2. Obtener todas las recetas**

**GET** `/recipes`
**Respuesta:**
200 OK + lista paginada o completa (según tu implementación)

---

## ➤ **3. Obtener receta por ID**

**GET** `/recipes/{id}`
**Respuesta:**
200 OK
404 si no existe

---

## ➤ **4. Actualizar receta**

**PUT** `/recipes/{id}`
**Respuesta:**
200 OK
404 si no existe

---

## ➤ **5. Eliminar receta**

**DELETE** `/recipes/{id}`
**Respuesta:**
204 NO CONTENT
404 si no existe

---

## ➤ **6. Buscar recetas por filtros (opcional si lo tienes)**

**GET** `/recipes/search`
**Query params (ejemplo):**

```
?title=pollo&difficultyId=2&categoryId=3&flavorId=1
```

**Respuesta:**
200 OK + recetas filtradas

---

# Tecnologías utilizadas

* Java / Spring Boot
* Spring JPA / Hibernate
* MySQL / PostgreSQL
* Lombok
* Maven
