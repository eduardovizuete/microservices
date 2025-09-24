// Conexión y creación de la base de datos
db = db.getSiblingDB('db_notify');

// Creación de la colección 'notify_orders'
db.createCollection('notify_orders');