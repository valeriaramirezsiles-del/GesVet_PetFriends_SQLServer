-- GesVet | Veterinaria PetFriends
-- Microsoft SQL Server (T-SQL). Ejecutar en SQL Server Management Studio.
-- No elimina datos o tablas existentes.
IF DB_ID(N'gesvet_petfriends') IS NULL
BEGIN
    CREATE DATABASE gesvet_petfriends;
END;
GO
USE gesvet_petfriends;
GO

IF OBJECT_ID(N'dbo.propietarios', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.propietarios (
        id_propietario INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        nombre NVARCHAR(150) NOT NULL,
        cedula NVARCHAR(30) NOT NULL UNIQUE,
        telefono NVARCHAR(30) NULL,
        correo NVARCHAR(150) NULL,
        activo BIT NOT NULL CONSTRAINT df_propietarios_activo DEFAULT 1,
        fecha_registro DATETIME2 NOT NULL CONSTRAINT df_propietarios_fecha DEFAULT SYSDATETIME()
    );
END;
GO

IF OBJECT_ID(N'dbo.mascotas', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.mascotas (
        id_mascota INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        nombre NVARCHAR(120) NOT NULL,
        tipo NVARCHAR(80) NOT NULL,
        raza NVARCHAR(100) NULL,
        edad INT NULL,
        observaciones NVARCHAR(255) NULL,
        id_propietario INT NOT NULL,
        activo BIT NOT NULL CONSTRAINT df_mascotas_activo DEFAULT 1,
        fecha_registro DATETIME2 NOT NULL CONSTRAINT df_mascotas_fecha DEFAULT SYSDATETIME(),
        CONSTRAINT fk_mascota_propietario FOREIGN KEY(id_propietario) REFERENCES dbo.propietarios(id_propietario)
    );
END;
GO

IF OBJECT_ID(N'dbo.servicios', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.servicios (
        id_servicio INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        nombre NVARCHAR(130) NOT NULL,
        descripcion NVARCHAR(255) NULL,
        precio DECIMAL(10,2) NULL,
        activo BIT NOT NULL CONSTRAINT df_servicios_activo DEFAULT 1
    );
END;
GO

IF OBJECT_ID(N'dbo.usuarios', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.usuarios (
        id_usuario INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        nombre_usuario NVARCHAR(80) NOT NULL UNIQUE,
        clave_hash NVARCHAR(255) NOT NULL,
        rol NVARCHAR(20) NOT NULL,
        activo BIT NOT NULL CONSTRAINT df_usuarios_activo DEFAULT 1,
        fecha_creacion DATETIME2 NOT NULL CONSTRAINT df_usuarios_fecha DEFAULT SYSDATETIME(),
        CONSTRAINT chk_usuarios_rol CHECK(rol IN (N'ADMIN',N'VETERINARIO'))
    );
END;
GO

IF OBJECT_ID(N'dbo.veterinarios', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.veterinarios (
        id_veterinario INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        id_usuario INT NOT NULL UNIQUE,
        nombre NVARCHAR(150) NOT NULL,
        especialidad NVARCHAR(120) NULL,
        telefono NVARCHAR(30) NULL,
        activo BIT NOT NULL CONSTRAINT df_veterinarios_activo DEFAULT 1,
        CONSTRAINT fk_veterinario_usuario FOREIGN KEY(id_usuario) REFERENCES dbo.usuarios(id_usuario)
    );
END;
GO

IF OBJECT_ID(N'dbo.citas', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.citas (
        id_cita INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        id_mascota INT NOT NULL,
        id_servicio INT NOT NULL,
        id_veterinario INT NULL,
        fecha DATE NOT NULL,
        hora TIME NOT NULL,
        estado NVARCHAR(20) NOT NULL CONSTRAINT df_citas_estado DEFAULT N'Pendiente',
        observaciones NVARCHAR(255) NULL,
        fecha_registro DATETIME2 NOT NULL CONSTRAINT df_citas_fecha DEFAULT SYSDATETIME(),
        CONSTRAINT fk_cita_mascota FOREIGN KEY(id_mascota) REFERENCES dbo.mascotas(id_mascota),
        CONSTRAINT fk_cita_servicio FOREIGN KEY(id_servicio) REFERENCES dbo.servicios(id_servicio),
        CONSTRAINT fk_cita_veterinario FOREIGN KEY(id_veterinario) REFERENCES dbo.veterinarios(id_veterinario),
        CONSTRAINT chk_estado_cita CHECK(estado IN (N'Pendiente',N'Confirmada',N'Atendida',N'Cancelada'))
    );
END;
GO
IF NOT EXISTS(SELECT 1 FROM sys.indexes WHERE name=N'idx_citas_fecha' AND object_id=OBJECT_ID(N'dbo.citas'))
    CREATE INDEX idx_citas_fecha ON dbo.citas(fecha,hora);
IF NOT EXISTS(SELECT 1 FROM sys.indexes WHERE name=N'idx_citas_veterinario' AND object_id=OBJECT_ID(N'dbo.citas'))
    CREATE INDEX idx_citas_veterinario ON dbo.citas(id_veterinario,fecha,hora);
GO

IF OBJECT_ID(N'dbo.atenciones', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.atenciones (
        id_atencion INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        id_cita INT NOT NULL UNIQUE,
        id_mascota INT NOT NULL,
        diagnostico NVARCHAR(MAX) NOT NULL,
        tratamiento NVARCHAR(MAX) NULL,
        observaciones NVARCHAR(MAX) NULL,
        fecha_atencion DATETIME2 NOT NULL CONSTRAINT df_atenciones_fecha DEFAULT SYSDATETIME(),
        CONSTRAINT fk_atencion_cita FOREIGN KEY(id_cita) REFERENCES dbo.citas(id_cita),
        CONSTRAINT fk_atencion_mascota FOREIGN KEY(id_mascota) REFERENCES dbo.mascotas(id_mascota)
    );
END;
GO

IF OBJECT_ID(N'dbo.consultas_contacto', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.consultas_contacto (
        id_consulta INT IDENTITY(1,1) NOT NULL PRIMARY KEY,
        nombre NVARCHAR(150) NOT NULL,
        correo NVARCHAR(150) NOT NULL,
        telefono NVARCHAR(30) NULL,
        motivo NVARCHAR(150) NULL,
        mensaje NVARCHAR(MAX) NOT NULL,
        fecha_envio DATETIME2 NOT NULL CONSTRAINT df_contacto_fecha DEFAULT SYSDATETIME()
    );
END;
GO

IF NOT EXISTS (SELECT 1 FROM dbo.servicios WHERE nombre=N'Consulta general')
    INSERT dbo.servicios (nombre,descripcion,precio,activo) VALUES(N'Consulta general',N'Revisión veterinaria general',0,1);
IF NOT EXISTS (SELECT 1 FROM dbo.servicios WHERE nombre=N'Vacunación')
    INSERT dbo.servicios (nombre,descripcion,precio,activo) VALUES(N'Vacunación',N'Aplicación de vacunas',0,1);
IF NOT EXISTS (SELECT 1 FROM dbo.servicios WHERE nombre=N'Desparasitación')
    INSERT dbo.servicios (nombre,descripcion,precio,activo) VALUES(N'Desparasitación',N'Control de parásitos',0,1);
IF NOT EXISTS (SELECT 1 FROM dbo.servicios WHERE nombre=N'Peluquería')
    INSERT dbo.servicios (nombre,descripcion,precio,activo) VALUES(N'Peluquería',N'Higiene y cuidado de la mascota',0,1);
GO
SELECT name AS tabla FROM sys.tables ORDER BY name;
GO
