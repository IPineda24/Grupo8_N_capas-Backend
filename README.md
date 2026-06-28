# 🏥 Telemedicina Backend
API REST para sistema de Gestión de Citas Médicas (Telemedicina), desarrollada con Spring Boot 3 + Java 21.

## 🚀 Tech Stack
- **Framework:** Spring Boot 3.4.5
- **Lenguaje:** Java 21
- **Build:** Gradle 8.13
- **Base de datos:** PostgreSQL 17
- **ORM:** Spring Data JPA + Hibernate 6
- **Migraciones:** Flyway
- **Seguridad:** Spring Security + JWT (jjwt 0.12.6)
- **Documentación:** SpringDoc OpenAPI (Swagger UI)
- **Contenedores:** Docker + Docker Compose
- **Despliegue:** Railway

## 👥 Integrantes
| Nombre | Carnet | Módulo |
|--------|--------|--------|
| Irvin Josué Pineda Molina | 00024420 | Auth + Seguridad JWT + Usuarios |
| Samael Atlacatl Montes Grande | 00061820 | Doctores + Especialidades + Horarios |
| Edson Enrique Palacios Quinteros | 00376920 | Citas + Recetas + Notas Médicas + Admin |

## 🌐 Sistema desplegado
https://grupo8-n-capas-backend.up.railway.app

## 📖 Documentación API

### Swagger UI
https://grupo8-n-capas-backend.up.railway.app/swagger-ui/index.html

### Colección Bruno
Importar la carpeta `DOCUMENTS/Telemedicina_API/` en Bruno y seleccionar el environment `local`.
Ejecutar **Login Admin** primero — el token se guarda automáticamente.

## 👤 Roles del sistema
| Rol | Descripción |
|-----|-------------|
| `ROLE_ADMIN` | Gestiona doctores, especialidades y configura normativas |
| `ROLE_DOCTOR` | Atiende citas, emite recetas y notas médicas |
| `ROLE_PATIENT` | Reserva citas, cancela, accede a sus recetas |

## 🔑 Endpoints
| Método | Ruta | Rol | Descripción |
|--------|------|-----|-------------|
| POST | `/api/auth/login` | Público | Login → JWT |
| POST | `/api/auth/register` | Público | Registro de paciente |
| GET | `/api/specialties` | Público | Listar especialidades |
| POST | `/api/specialties` | ADMIN | Crear especialidad |
| PUT | `/api/specialties/{id}` | ADMIN | Actualizar especialidad |
| DELETE | `/api/specialties/{id}` | ADMIN | Desactivar especialidad |
| GET | `/api/doctors` | Público | Listar doctores |
| GET | `/api/doctors/specialty/{id}` | Público | Doctores por especialidad |
| GET | `/api/doctors/{id}/schedule` | Público | Horario del doctor |
| GET | `/api/doctors/{id}/availability` | Público | Disponibilidad por fecha |
| POST | `/api/doctors` | ADMIN | Crear doctor |
| POST | `/api/doctors/schedule` | ADMIN | Agregar horario al doctor |
| GET | `/api/patients` | ADMIN | Listar pacientes |
| GET | `/api/patients/me` | PATIENT | Ver mi perfil |
| PUT | `/api/patients/me` | PATIENT | Actualizar mi perfil |
| GET | `/api/patients/{id}` | ADMIN, DOCTOR | Ver paciente por ID |
| GET | `/api/patients/{id}/history` | ADMIN, DOCTOR, PATIENT | Historial clínico completo |
| POST | `/api/appointments` | PATIENT | Crear cita |
| GET | `/api/appointments` | ADMIN | Ver todas las citas |
| GET | `/api/appointments/my` | PATIENT | Ver mis citas |
| GET | `/api/appointments/doctor-agenda` | DOCTOR | Ver agenda del doctor |
| PUT | `/api/appointments/{id}/complete` | DOCTOR | Completar cita |
| DELETE | `/api/appointments/{id}/cancel` | PATIENT, DOCTOR | Cancelar cita |
| PUT | `/api/appointments/{id}/rate` | PATIENT | Calificar cita |
| POST | `/api/appointments/{id}/attachments` | PATIENT, DOCTOR | Adjuntar documento |
| GET | `/api/appointments/{id}/attachments` | PATIENT, DOCTOR | Ver adjuntos de la cita |
| POST | `/api/prescriptions` | DOCTOR | Emitir receta |
| GET | `/api/prescriptions/my` | PATIENT | Ver mis recetas |
| GET | `/api/prescriptions/appointment/{id}` | DOCTOR, PATIENT | Recetas de una cita |
| PUT | `/api/prescriptions/{id}/use` | PATIENT | Usar receta |
| POST | `/api/medical-notes` | DOCTOR | Crear nota médica |
| GET | `/api/medical-notes/appointment/{id}` | DOCTOR, PATIENT | Notas de una cita |
| GET | `/api/admin/reports/doctors` | ADMIN | Reporte doctores más solicitados |

## ⚙️ Configuración rápida

### Prerequisitos
- Docker Desktop instalado y corriendo
- Java 21

### Levantar con el script incluido (recomendado)
```bash
# Windows
.\start.ps1

# Detener
.\stop.ps1
```

### Levantar con Docker Compose completo
```bash
# Compila y levanta todo (BD + App) en un solo comando
docker compose up --build -d

# Ver logs
docker compose logs -f app

# Detener
docker compose down
```

### Levantar manualmente
```bash
# 1. Levantar base de datos
docker compose up db -d

# 2. Correr la aplicación
.\gradlew.bat bootRun       # Windows
./gradlew bootRun           # Linux/Mac
```

### Variables de entorno
El proyecto incluye valores por defecto para desarrollo local. Para producción configurar:

JWT_SECRET=<base64-encoded-secret-min-256bits>

SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:<port>/<database>

SPRING_DATASOURCE_USERNAME=postgres

SPRING_DATASOURCE_PASSWORD=<password>


## 👤 Credenciales por defecto
| Rol | Email | Password |
|-----|-------|----------|
| ADMIN | `admin@telemedicina.com` | `Admin1234!` |
| DOCTOR | `dra.gonzalez@telemedicina.com` | `Password123!` |
| PATIENT | `pacheco@gmail.com` | `Password123!` |

## 📋 Reglas de negocio implementadas
- ✅ Control de doble reserva (paciente y doctor)
- ✅ Citas solo en intervalos de 30 minutos (08:00, 08:30, 09:00...)
- ✅ Validación de horario del doctor por día de la semana
- ✅ Sin horarios duplicados por doctor y día
- ✅ Búsqueda de disponibilidad por fecha y especialidad
- ✅ Cancelación con 4h+ de anticipación → reembolso del 80%
- ✅ Validación de edad mínima por especialidad
- ✅ Enlace de videollamada generado automáticamente
- ✅ Receta digital firmada con SHA-256 y máximo 3 usos
- ✅ Receta con fecha de expiración
- ✅ Notas médicas por cita accesibles por doctor y paciente
- ✅ Historial clínico completo del paciente
- ✅ Adjuntar documentos por cita
- ✅ Pago simulado (compatible con Stripe)
- ✅ Bloqueo optimista con `@Version` en Appointment
- ✅ Migraciones automáticas con Flyway

## 🌿 Flujo Git (Gitflow)

└── develop

├── feature/auth-security              (Irvin Pineda)

├── feature/doctors-specialties        (Samael Montes)

└── feature/appointments-prescriptions (Edson Palacios)
