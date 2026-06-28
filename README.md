# 🏥 Telemedicina Backend

API REST para sistema de Gestión de Citas Médicas (Telemedicina), desarrollada con Spring Boot 4 + Java 21.

## 🚀 Tech Stack
- **Framework:** Spring Boot 4.0.5
- **Lenguaje:** Java 21
- **Build:** Gradle
- **Base de datos:** PostgreSQL 17
- **ORM:** Spring Data JPA + Flyway
- **Seguridad:** Spring Security + JWT (jjwt 0.12.6)
- **CI/CD:** GitHub Actions
- **Contenedores:** Docker + Docker Compose

## 👥 Roles
| Rol | Descripción |
|-----|-------------|
| `ROLE_ADMIN` | Gestiona doctores, especialidades y configura normativas |
| `ROLE_DOCTOR` | Atiende citas, escribe recetas, accede al historial |
| `ROLE_PATIENT` | Reserva citas, cancela, accede a sus recetas |

## 🔑 Endpoints principales
| Método | Ruta | Rol | Descripción |
|--------|------|-----|-------------|
| POST | `/api/auth/login` | Público | Login → JWT |
| POST | `/api/auth/register` | Público | Registro de paciente |
| GET | `/api/specialties` | Público | Listar especialidades |
| GET | `/api/doctors` | Público | Listar doctores |
| POST | `/api/doctors` | ADMIN | Crear doctor |
| POST | `/api/appointments` | PATIENT | Crear cita |
| DELETE | `/api/appointments/{id}/cancel` | AUTH | Cancelar cita |
| PUT | `/api/appointments/{id}/rate` | PATIENT | Calificar cita |
| POST | `/api/prescriptions` | DOCTOR | Emitir receta |
| PUT | `/api/prescriptions/{id}/use` | PATIENT | Usar receta |
| POST | `/api/medical-notes` | DOCTOR | Crear nota médica |
| GET | `/api/admin/reports/doctors` | ADMIN | Reporte de doctores |

## ⚙️ Configuración rápida

### Variables de entorno requeridas
```
JWT_SECRET=<base64-encoded-secret-min-256bits>
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/telemedicina
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=root
```

### Levantar con Docker Compose
```bash
docker compose up -d
```

## 👤 Usuario admin por defecto
- **Email:** admin@telemedicina.com
- **Password:** Admin1234

## 📋 Reglas de negocio implementadas
- ✅ Control de doble reserva (paciente y doctor)
- ✅ Cancelación hasta 4h antes → reembolso 80%
- ✅ Receta digital con firma SHA-256 (máx. 3 usos)
- ✅ Validación de edad mínima por especialidad
- ✅ Enlace de videollamada generado automáticamente
- ✅ Historial clínico accesible por médico y paciente
- ✅ Bloqueo optimista con @Version en Appointment
- ✅ Pago simulado (compatible con Stripe)
