# API-de-Firma-Digital-Simple
Una API REST que permite a los usuarios firmar digitalmente documentos y verificar dichas firmas.

1. Para desplegar la aplicación:
  - Descargar repositorio
  - Tener Docker instalado
  - ejecutar "docker-compose up --build -d" en la carpeta raíz del proyecto (donde se encuentra el docker-compose.yml)

2.1 Para probar la aplicación con Postman:
  - Tipo de autenticación: Basic Auth (las credenciales son necesarias para ciertos métodos, de ahora en adelante mencionados como "securizado")
  - Endpoints expuestos:
    1. Resigtrar usuario:
        POST: localhost:8080/usuarios/registrar?nombre=[nombre del usuario]&contrasena=[contraseña del usuario]
       
    2. Autenticar usuario:
        POST: localhost:8080/usuarios/autenticar?nombre=[nombre del usuario]&contrasena=[contraseña del usuario]
       
    3. Generar claves (securizado):
        POST: localhost:8080/usuarios/[nombre del usuario]/generar-claves
       
    4. Firmar documento (securizado):
        POST: localhost:8080/firma-digital/firmar?usuario=[nombre del usuario]
         - body: [texto plano en Base64 del documento a firmar]   
    5. Verificar firma:
        POST: localhost:8080/firma-digital/verificar-documento
         - body: JSON con el siguiente formato:
           {
              "usuarioId": [id del usuario (obtenido al autenticar usuario)],
              "documentoBase64": "[documento en Base64]",
              "firmaBase64": "[firma en Base64]"
           }

2.2 Para probar la aplicación con cURL:
  - Tipo de autenticación: Basic Auth (las credenciales son necesarias para ciertos métodos, de ahora en adelante mencionados como "securizado").
  - Endpoints expuestos:
    1. Registrar usuario:
      curl -X POST "http://localhost:8080/usuarios/registrar?nombre=[nombre del usuario]&contrasena=[contraseña del usuario]"

    2. Autenticar usuario:
      curl -X POST "http://localhost:8080/usuarios/autenticar?nombre=[nombre del usuario]&contrasena=[contraseña del usuario]"

    3. Generar claves (securizado):
      curl -X POST -u [nombre del usuario]:[contraseña] "http://localhost:8080/usuarios/[nombre del usuario]/generar-claves"

    4. Firmar documento (securizado):
      curl -X POST -u [nombre del usuario]:[contraseña] "http://localhost:8080/firma-digital/firmar?usuario=[nombre del usuario]" \
        -d "[texto plano del documento en Base64 a firmar]"
    
    5. Verificar firma:
      curl -X POST "http://localhost:8080/firma-digital/verificar-documento" \
        -H "Content-Type: application/json" \
        -d '{
            "usuarioId": [id del usuario (obtenido al autenticar usuario)],
            "documentoBase64": "[documento en Base64]",
            "firmaBase64": "[firma en Base64]"
            }'
