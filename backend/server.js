const express = require('express');
const { Pool } = require('pg');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(express.json());

// Conexiunea la Docker
const pool = new Pool({
    user: 'postgres',
    host: 'localhost',
    database: 'courtconnect',
    password: 'password',
    port: 5433,
});

// Creează tabelul dacă nu există deja
pool.query(`
    CREATE TABLE IF NOT EXISTS matches (
        id SERIAL PRIMARY KEY,
        "creatorName" VARCHAR(100),
        "clubName" VARCHAR(100),
        "ntrpLevel" NUMERIC,
        "matchType" VARCHAR(50),
        "date" VARCHAR(50)
    )
`).catch(err => console.log(err));

// READ
app.get('/api/matches', async (req, res) => {
    const result = await pool.query('SELECT * FROM matches ORDER BY id ASC');
    res.json(result.rows);
});

// CREATE
app.post('/api/matches', async (req, res) => {
    const { creatorName, clubName, ntrpLevel, matchType, date } = req.body;
    const result = await pool.query(
        'INSERT INTO matches ("creatorName", "clubName", "ntrpLevel", "matchType", "date") VALUES ($1, $2, $3, $4, $5) RETURNING *',
        [creatorName, clubName, ntrpLevel, matchType, date]
    );
    res.json(result.rows[0]);
});

// UPDATE
app.put('/api/matches/:id', async (req, res) => {
    const { id } = req.params;
    const { creatorName, clubName, ntrpLevel, matchType, date } = req.body;
    const result = await pool.query(
        'UPDATE matches SET "creatorName" = $1, "clubName" = $2, "ntrpLevel" = $3, "matchType" = $4, "date" = $5 WHERE id = $6 RETURNING *',
        [creatorName, clubName, ntrpLevel, matchType, date, id]
    );
    res.json(result.rows[0]);
});

// DELETE
app.delete('/api/matches/:id', async (req, res) => {
    const { id } = req.params;
    await pool.query('DELETE FROM matches WHERE id = $1', [id]);
    res.send({ success: true });
});

app.listen(5000, () => console.log('Serverul Node.js rulează pe portul 5000!'));