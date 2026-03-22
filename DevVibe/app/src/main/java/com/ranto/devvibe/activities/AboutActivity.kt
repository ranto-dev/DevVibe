/**
 * Activity: AboutActivity
 *
 * Description :
 * Cette activite gere le rendu de la page "À propos"
 *
 * Fonctionnalités :
 * Elle permet à l'utilisateur de
 * — connaitre des informations sur l'application
 * — savoir toutes les fonctionnalités disponibles actuelles
 * — savoir la version
 *
 * Composants principaux :
 * — card en header
 * — quatre (4) sections dédiées :
 *  . Une description "À propos",
 *  . Presentation des features,
 *  . Une section qui informe la version de l'application
 *  . Information sur son developpement
 *
 *  Cycle de vie :
 *  — onCreate: initialisation des vues et listeners
 *
 *  Auteur : Ranto Andrianandraina
 */
package com.ranto.devvibe.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ranto.devvibe.R

class AboutActivity : AppCompatActivity() {
    /**
     * Initialisation de l'activité
     * - Chargement du layout
     * - Initialisation des composants UI
     * - Configuration des événements
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_about)
    }
}