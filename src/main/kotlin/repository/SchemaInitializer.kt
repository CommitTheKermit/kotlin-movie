package repository

import java.sql.Connection
object SchemaInitializer {
    fun initialize(connection: Connection) {
        val sql = this::class.java.getResource("/schema.sql")!!.readText()
        connection.createStatement().use { it.execute(sql) }
    }
}
