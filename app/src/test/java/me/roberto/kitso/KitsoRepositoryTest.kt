package me.roberto.kitso

import me.roberto.kitso.repository.KitsoRepository
import org.junit.Test

import org.junit.Assert.*

class KitsoRepositoryTest {

    val kitsoRepository= KitsoRepository()

    @Test
    fun getHistoricData() {
    }



    @Test
    fun getAvailableBooksandBook()
    {
        val testSubscriber = kitsoRepository.getAvailableBooks().test()
        testSubscriber.assertNoErrors().awaitTerminalEvent()

        val values=testSubscriber.values()

        val availableBooks=values[0]

        for (bookItem in availableBooks) {



            assertNotNull(bookItem)

            val subscriber=kitsoRepository.getBook(bookItem.book!!).test()
            subscriber.assertNoErrors().awaitTerminalEvent()
            val book = subscriber.values()[0]

            println("${book.book} - ${book.last}")

        }



    }


    @Test
    fun getBook() {

        val testSubscriber = kitsoRepository.getBook("eth_mxn").test()
        testSubscriber.assertNoErrors().awaitTerminalEvent()

        val book=testSubscriber.values()[0]
                assertNotNull(book)
                println("${book.book} , ${book.last}")

    }

    @Test
    fun getAvailableBooks(){

        val testSubscriber = kitsoRepository.getAvailableBooks().test()
                testSubscriber.assertNoErrors().awaitTerminalEvent()

                val values=testSubscriber.values()

        val availableBooks=values[0]

        for (bookItem in availableBooks) {

                assertNotNull(bookItem)
                assertNotNull(bookItem.minimumAmount)
                assertNotNull(bookItem.maximumAmount)
                assertNotNull(bookItem.maximumPrice)
                assertNotNull(bookItem.minimumPrice)
                assertNotNull(bookItem.minimumValue)
                assertNotNull(bookItem.maximumValue)
                println("${bookItem.book} , ${bookItem.maximumAmount}, ${bookItem.minimumAmount}")


        }

    }
}