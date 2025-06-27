// Conexion y creacion de la base de datos
db = db.getSiblingDB('db_notify');

// Creacion de la coleccion 'notify_orders'
db.createCollection('notify_orders');
