/**
 * Nom : HelpActivity
 *
 * Description :
 * Cette activité gere le rendu de l'interface d'aide
 *
 * Fonctionnalités :
 * Elle a pour role de guide les utilisateurs pour l'utilisation de l'application
 *
 * Composants principaux :
 * – une section banner (HEADER) qui illustre à l'utilisateur cette interface
 * — dans cette interface, on peut constater quatre (4) card dont chacun à des informations d'aide pour les quatre fonctionnalités clés de l'application actuel
 * — une section deed a la retour utilisateur ou feedback des utilisateurs pour l'évolutivité de l'application (en cours de development)
 *
 * Cycle de vie :
 * — onCreate : Initialisation des vues et listeners
 *
 * Auteur : Ranto Andrianandraina
 */

package com.ranto.devvibe.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.ranto.devvibe.R

class HelpActivity : AppCompatActivity() {
    /**
     * Initialisation de l'activité
     * - Chargement du layout
     * - Initialisation des composants UI
     * - Configuration des événements
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_help)
    }
}