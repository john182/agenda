package com.chronos.agenda.model

import java.io.Serializable
import java.math.BigDecimal

/**
 * Created by John Vanderson M L on 25/02/2018.
 */
class Aluno(
        var id: String = "",
        var nome: String,
        var endereco: String,
        var telefone: String,
        var site: String,
        var nota: BigDecimal,
        var caminhoFoto: String) : Serializable {
    
   

    override fun toString(): String {
        return id + " - " + nome
    }

    constructor() : this("","","","","", BigDecimal.ZERO,"")
}
