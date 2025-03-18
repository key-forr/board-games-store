package com.example.boardGamesStore.data.repository

import androidx.lifecycle.LiveData
import com.example.boardGamesStore.data.dao.OrderDao
import com.example.boardGamesStore.data.entity.Order
import com.example.boardGamesStore.data.entity.OrderItem
import com.example.boardGamesStore.data.entity.OrderWithItems
import java.util.Date

class OrderRepository(private val orderDao: OrderDao) {
    suspend fun createOrder(order: Order): Long {
        return orderDao.insertOrder(order)
    }

    suspend fun addOrderItem(orderItem: OrderItem): Long {
        return orderDao.insertOrderItem(orderItem)
    }

    suspend fun getOrderById(orderId: Long): Order? {
        return orderDao.getOrderById(orderId)
    }

    suspend fun getOrderWithItems(orderId: Long): OrderWithItems? {
        return orderDao.getOrderWithItems(orderId)
    }

    fun getOrdersForUser(userId: Long): LiveData<List<OrderWithItems>> {
        return orderDao.getOrdersForUser(userId)
    }

    fun getAllOrders(): LiveData<List<OrderWithItems>> {
        return orderDao.getAllOrders()
    }

    suspend fun updateOrderStatus(orderId: Long, status: String) {
        orderDao.updateOrderStatus(orderId, status, Date())
    }
}