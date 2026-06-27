-- crear nuestra base de datos
create database if not exists bd_cafeteria;
-- usamos la base de datos
use bd_cafeteria;

-- crear tabla usuario
create table tb_usuario(
    idUsuario  int auto_increment primary key,
    nombre     varchar(30)  not null,
    apellido   varchar(30)  not null,
    usuario    varchar(15)  not null unique,
    password   varchar(15)  not null,
    telefono   varchar(15),
    estado     int  not null
);

-- crear tabla cliente
create table tb_cliente(
    idCliente  int       auto_increment primary key,
    nombre     varchar(30)   not null,
    apellido   varchar(30)   not null,
    DNI        varchar(15)   not null unique,
    telefono   varchar(15)   not null,
    direccion  varchar(100)  not null,
    estado     int        not null
);
 
-- crear tabla categoria
create table tb_categoria(
    idCategoria  int       auto_increment primary key,
    descripcion  varchar(200)  not null unique,
    estado       int        not null
);

-- crear tabla producto
create table tb_producto(
    idProducto   int       auto_increment primary key,
    nombre       varchar(100)  not null,
    cantidad     int       not null,
    precio       decimal(10,2)  not null,
    descripcion  varchar(200)  not null,
    idCategoria  int       not null ,
    estado       int        not null,
    constraint fk_tb_producto_tb_categoria Foreign key (idCategoria) references tb_categoria(idCategoria),
    unique(idCategoria, descripcion)
);

-- crear tabla cabecera venta
create table tb_cabecera_venta(
    idCabeceraVenta  int      auto_increment primary key,
    idCliente        int      not null,
    idUsuario        int      not null,
    valorPagar       decimal(10,2) not null,
    fechaVenta       date         not null,
    estado           int       not null,
    constraint fk_tb_cabecera_venta_tb_cliente Foreign key (idCliente) references tb_cliente(idCliente),
    constraint fk_tb_cabecera_venta_tb_usuario Foreign key (idUsuario) references tb_usuario(idUsuario)
);
 
-- crear tabla detalle venta
create table tb_detalle_venta(
    idDetalleVenta   int      auto_increment primary key,
    idCabeceraVenta  int      not null,
    idProducto       int      not null,
    cantidad         int      not null,
    precioUnitario   decimal(10,2) not null,
    subtotal         decimal(10,2) not null,
    descuento        decimal(10,2) not null,
    totalPagar       decimal(10,2) not null,
    estado           int       not null,
    constraint fk_tb_detalle_venta_tb_cabecera_venta Foreign key (idCabeceraVenta) references tb_cabecera_venta(idCabeceraVenta),
    constraint fk_tb_detalle_venta_tb_producto Foreign key (idProducto) references tb_producto(idProducto)
);
 
-- Registrar datos iniciales
insert into tb_usuario(nombre, apellido, usuario, password, telefono, estado)
values ("Alexander", "Ormeño", "alex", "12345", "965589125", 1);

insert into tb_cliente(nombre, apellido, dni, telefono, direccion, estado) 
VALUES('Lorena', 'Matta Baca', '22223344', '38279222', 'Ica', 1);

insert into tb_categoria(descripcion, estado)
values ("Postres", "1");

insert into tb_producto(nombre, descripcion, estado, idcategoria, precio, cantidad)
values ("Torta selva negra", "", 1, 1, 5, 50);


-- consultas opcionales (no es necesario ejecutar)
select * from tb_categoria;

SELECT idproducto, nombre, cantidad, precio, tb_producto.descripcion, tb_producto.idcategoria, 
tb_producto.estado, tb_categoria.descripcion as categoria
FROM tb_producto inner join tb_categoria ON tb_producto.idcategoria = tb_categoria.idcategoria
ORDER BY nombre;

select * from tb_cabecera_venta;
select * from tb_detalle_venta;