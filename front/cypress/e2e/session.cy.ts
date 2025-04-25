/// <reference types="cypress" />

describe('Sessions spec', () => {
    beforeEach(() => {
        cy.getSessions()
        
        cy.login()
    })

    it('Displays sessions', () => {
          // Vérifie que les sessions s'affichent bien
          cy.contains('yoga du matin').should('be.visible')
          cy.contains('pilates débutant').should('be.visible')
    })

    it('Displays create and detail button if admin', () => {
        // Vérifie que le bouton "Create" est visible pour les admins
        cy.contains('button', 'Create').should('be.visible')

        // Vérifie que le bouton "Detail" est visible pour "Yoga du matin"
        cy.get('mat-card.item')
        .contains('yoga du matin')
        .parents('mat-card.item') // remonte à la carte parent
        .find('button')
        .contains('Detail')
        .should('be.visible')

        // Vérifie aussi le bouton pour "Pilates débutant"
        cy.get('mat-card.item')
        .contains('pilates débutant')
        .parents('mat-card.item')
        .find('button')
        .contains('Detail')
        .should('be.visible')
    })
    
    it('Displays session details when clicking on Detail button', () => {

        cy.getTeacher()
        // Intercepte la navigation vers la page de détail
        cy.getSessionDetail()
      
        // Clique sur le bouton "Detail" de la session "Yoga du matin"
        cy.get('mat-card.item')
          .contains('yoga du matin')
          .parents('mat-card.item')
          .find('button')
          .contains('Detail')
          .click()
      
        // Attendre la requête réseau si besoin
        cy.wait('@getSessionDetail')
      
        // Vérifie l’URL de la page de détails (ex: /sessions/detail/1)
        cy.url().should('include', '/sessions/detail/1')
      
        cy.get('h1').should('contain.text', 'Yoga Du Matin')
        cy.get('.description').should('contain.text', 'Yoga pour bien commencer la journée')
        cy.get('.created').should('contain.text', 'April 24, 2025')
        cy.get('.updated').should('contain.text', 'April 28, 2025')
        cy.get('mat-card-subtitle').should('contain.text', 'Margot DELAHAYE')
        // cy.get('span.ml1').should('contain.text', '2025-04-24') // Date de la session
        // cy.get('span.ml1').should('contain.text', '20 avril 2025')
      })

      it('Display dete button', () => {
        cy.getSessionDetail()

        // Clique sur le bouton "Detail" de la session "Yoga du matin"
        cy.get('mat-card.item')
        .contains('yoga du matin')
        .parents('mat-card.item')
        .find('button')
        .contains('Detail')
        .click()

          cy.get('mat-card-title')
            .find('button')
            .contains('Delete')
            .should('not.be.disabled')
      })
})