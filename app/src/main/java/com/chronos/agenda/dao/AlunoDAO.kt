package com.chronos.agenda.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils
import com.chronos.agenda.model.Aluno
import java.util.*


/**
 * Created by alura on 12/08/15.
 */
class AlunoDAO(context: Context) : SQLiteOpenHelper(context, "Agenda", null, 5) {

    override fun onCreate(db: SQLiteDatabase) {
        val sql = "CREATE TABLE Alunos (id char(36) PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "telefone TEXT, " +
                "site TEXT, " +
                "nota REAL, " +
                "caminhoFoto TEXT);"
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        when (oldVersion) {
            1 -> {
              val  sql = "ALTER TABLE Alunos ADD COLUMN caminhoFoto TEXT"
                db.execSQL(sql) // indo para versao 2
            }
            2->{
                val sql = "CREATE TABLE Alunos_novo (id char(36) PRIMARY KEY, " +
                        "nome TEXT NOT NULL, " +
                        "endereco TEXT, " +
                        "telefone TEXT, " +
                        "site TEXT, " +
                        "nota REAL, " +
                        "caminhoFoto TEXT);"
                db.execSQL(sql)
                val copi = "INSERT INTO Alunos_novo "+
                        "(id,nome,endereco,telefone,site,nota,caminhoFoto)"+
                        " SELECT * FROM Alunos"
                db.execSQL(copi)

                val removerTabela = "DROP TABLE Alunos;"
                db.execSQL(removerTabela)

                val renomearTabela = "ALTER TABLE ALunos_novo RENAME TO Alunos"

                db.execSQL(renomearTabela)
            }
            3->{
                val updateId = "SELECT * FROM Alunos"

                val cursor = db.rawQuery(updateId, null)
                val atualizarId = "UPDATE Alunos SET id = ? WHERE id = ?"
                while (cursor.moveToNext()){
                    val aluno = popularAluno(cursor)
                    val params = arrayOf(gerarUUID(), aluno.id)
                    db.execSQL(atualizarId,params)
                }
            }
            4->{
                val deleteIdNull = "DELETE FROM Alunos WHERE id IS NULL"
                db.execSQL(deleteIdNull)
            }
        }

    }

    fun insere(aluno: Aluno):Aluno {
        val db = writableDatabase

        val dados = pegaDadosDoAluno(aluno)
        if(TextUtils.isEmpty(aluno.id)){
            aluno.id = gerarUUID()
        }
        db.insert("Alunos", null, dados)

        return aluno
    }

    fun sicronizarDados(alunos: List<Aluno>) {
        for (aluno in alunos) {
            if(existeAluno(aluno)){
                altera(aluno)
            }else{
                insere(aluno)
            }
        }
    }



    fun buscaAlunos(): List<Aluno> {
        val sql = "SELECT * FROM Alunos;"
        val db = readableDatabase
        val c = db.rawQuery(sql, null)

        val alunos = ArrayList<Aluno>()
        while (c.moveToNext()) {
            val aluno = popularAluno(c)
            alunos.add(aluno)
        }
        c.close()

        return alunos
    }

    private fun popularAluno(c: Cursor): Aluno {
        return Aluno(c.getString(c.getColumnIndex("id")),
                c.getString(c.getColumnIndex("nome")),
                c.getString(c.getColumnIndex("endereco")),
                c.getString(c.getColumnIndex("telefone")),
                c.getString(c.getColumnIndex("site")),
                c.getDouble(c.getColumnIndex("nota")).toBigDecimal(),
                c.getString(c.getColumnIndex("caminhoFoto")))
    }

    fun deleta(aluno: Aluno) {
        val db = writableDatabase

        val params = arrayOf(aluno.id)
        db.delete("Alunos", "id = ?", params)
    }

    fun altera(aluno: Aluno) {
        val db = writableDatabase

        val dados = pegaDadosDoAluno(aluno)

        val params = arrayOf(aluno.id)
        db.update("Alunos", dados, "id = ?", params)
    }

    fun ehAluno(telefone: String): Boolean {
        val db = readableDatabase
        val c = db.rawQuery("SELECT * FROM Alunos WHERE telefone = ?", arrayOf(telefone))
        val resultados = c.count
        c.close()
        return resultados > 0
    }
    private fun pegaDadosDoAluno(aluno: Aluno): ContentValues {
        val dados = ContentValues()
        dados.put("id", aluno.id)
        dados.put("nome", aluno.nome)
        dados.put("endereco", aluno.endereco)
        dados.put("telefone", aluno.telefone)
        dados.put("site", aluno.site)
        dados.put("nota", aluno.nota.toDouble())
        dados.put("caminhoFoto", aluno.caminhoFoto)
        return dados
    }

    private fun gerarUUID():String{
        return UUID.randomUUID().toString()
    }



    private fun existeAluno(aluno: Aluno): Boolean {
        val db = readableDatabase
        val sql  = " SELECT id FROM ALUNOS where id=? LIMIT 1"
        val params = listOf<String>(aluno.id)
        val cursor = db.rawQuery(sql, params.toTypedArray())
        return cursor.count > 0
    }
}
