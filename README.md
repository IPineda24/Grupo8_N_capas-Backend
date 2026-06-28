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
- **Contenedores:** Docker + Docker Compose

## 👥 Roles
| Rol | Descripción |
|-----|-------------|
| `ROLE_ADMIN` | Gestiona doctores, especialidades y configura normativas |
| `ROLE_DOCTOR` | Atiende citas, emite recetas y notas médicas |
| `ROLE_PATIENT` | Reserva citas, cancela, accede a sus recetas |

## 🔑 Endpoints principales
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
| POST | `/api/doctors` | ADMIN | Crear doctor |
| POST | `/api/doctors/schedule` | ADMIN | Agregar horario al doctor |
| GET | `/api/patients` | ADMIN | Listar pacientes |
| GET | `/api/patients/me` | PATIENT | Ver mi perfil |
| GET | `/api/patients/{id}` | ADMIN, DOCTOR | Ver paciente por ID |
| POST | `/api/appointments` | PATIENT | Crear cita |
| GET | `/api/appointments` | ADMIN | Ver todas las citas |
| GET | `/api/appointments/my` | PATIENT | Ver mis citas |
| GET | `/api/appointments/doctor-agenda` | DOCTOR | Ver agenda del doctor |
| PUT | `/api/appointments/{id}/complete` | DOCTOR | Completar cita |
| DELETE | `/api/appointments/{id}/cancel` | PATIENT, DOCTOR | Cancelar cita |
| PUT | `/api/appointments/{id}/rate` | PATIENT | Calificar cita |
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
