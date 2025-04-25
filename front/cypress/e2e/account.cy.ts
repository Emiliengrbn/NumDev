describe('Account spec', () => {
    it('Displays account informations', () => {

          cy.intercept('GET', '/api/user/1', {
            statusCode: 200,
            body: {
                id: 1,
                email: "yoga@studio.com",
                lastName: "Admin",
                firstName: "Admin",
                admin: true,
                createdAt: "2025-04-11T10:46:42",
                updatedAt: "2025-04-11T10:46:42"
            },
          }).as('me')
      
        cy.login()

      cy.get('span[routerLink="me"]').click()
      cy.url().should('include', '/me')
  
      // Vérifie le nom complet (avec lastName en uppercase)
      cy.contains('p', 'Name: Admin ADMIN')
  
      // Vérifie l'email
      cy.contains('p', 'Email: yoga@studio.com')
  
      // Vérifie que l'utilisateur est admin
      cy.contains('p', 'You are admin')
  
      // Vérifie la date de création
      cy.contains('p', 'Create at: April 11, 2025')
  
      // Vérifie la dernière mise à jour
      cy.contains('p', 'Last update: April 11, 2025')
  
      // Vérifie que le bouton "Delete my account" n'est **pas** visible pour un admin
      cy.contains('Delete my account:').should('not.exist')
    })
})