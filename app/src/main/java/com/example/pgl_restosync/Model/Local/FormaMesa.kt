package com.example.pgl_restosync.Model.Local

// Estas son las posibles formas de las mesas que puede tener el restaurante
// Uso la clase ENUM porque con la función creada para asignar las mesas por cada string, tiene un else que,
// si escribo algo mal, lanza el else con la imagen por defecto sin que me de cuenta del error. Esto evitará ese error
enum class FormaMesa {
    CUADRADA,
    REDONDA_CUATRO_ASIENTOS,
    REDONDA_CINCO_ASIENTOS,
    RECTANGULAR
}