## Test Agregados a Order

#### 1. createOrder:

- **Creación exitosa**
- **Restaurante no encontrado**
- **Cliente con pedido en proceso**

#### 2. getAllOrders:

- **Retorno exitoso de página de órdenes**

#### 2. getOrdersByStatus:

- **Status válido**
- **Status inválido**

#### 3. assignOrderToEmployee:

- **Asignación exitosa**
- **Orden no encontrada**

#### 4. updateOrderStatus:

- **Status LISTO (genera código y SMS)**
- **Orden ya entregada**
- **Intento de cambiar a ENTREGADO**

#### 5. deliverOrder:

- **Entrega exitosa con PIN correcto**
- **PIN incorrecto**
- **Orden no lista**
- **Orden no encontrada**