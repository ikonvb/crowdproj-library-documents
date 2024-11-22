package ru.otus.crowd.proj.docs.cards.api.v1.mappers.exceptions
class UnknownRequestError(clazz: Class<*>) : RuntimeException("Class $clazz cannot be mapped to DocCardContext")
