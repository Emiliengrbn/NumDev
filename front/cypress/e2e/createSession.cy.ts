describe('Create session spec', () => {
    beforeEach(() => {
      cy.visit('/login')
  
      // Simule une réponse de login réussie
      cy.intercept('POST', '/api/auth/login', {
        statusCode: 200,
        body: {
          id: 1,
          username: 'yogi123',
          firstName: 'Yoga',
          lastName: 'Lover',
          admin: true,
        },
      }).as('login')

      // Intercepter la requête GET pour récupérer les enseignants avec le jeton dans les en-têtes
      cy.intercept('GET', '/api/teacher', {
          statusCode: 200,
          body: [
            {
              id: 1,
              lastName: 'DELAHAYE',
              firstName: 'Margot',
              createdAt: '2025-04-24T14:20:04',
              updatedAt: '2025-04-24T14:20:04',
            },
            {
              id: 2,
              lastName: 'THIERCELIN',
              firstName: 'Hélène',
              createdAt: '2025-04-24T14:20:04',
              updatedAt: '2025-04-24T14:20:04',
            },
          ],
      }).as('getTeachers')
  
      cy.get('input[formControlName=email]').type('yogi@studio.com')
      cy.get('input[formControlName=password]').type('test!1234')
      cy.get('button[type=submit]').click()
  
      // Attendre la réponse d'authentification avant de continuer
      cy.wait('@login')
  
      // Vérifie que l'on est redirigé vers /sessions après la connexion
      cy.url().should('include', '/sessions')
  
      cy.get('button[routerLink="create"]').click()
      cy.url().should('include', '/sessions/create')
  
      
    })
  
    it('should not allow form submission if required fields are empty', () => {
      // Vérifier que tous les champs sont vides
      cy.get('input[formControlName="name"]').should('have.value', '')
      cy.get('input[formControlName="date"]').should('have.value', '')
      cy.get('mat-select[formControlName="teacher_id"]').should('not.have.value')
      cy.get('textarea[formControlName="description"]').should('have.value', '')
  
      cy.get('button[type="submit"]').should('be.disabled')
      cy.get('input[formControlName="name"]').type('Yoga Session')
      cy.get('button[type="submit"]').should('be.disabled')
      cy.get('input[formControlName="date"]').type('2025-04-24')
      cy.get('mat-select[formControlName="teacher_id"]').click()
      cy.get('mat-option').should('be.visible').first().click()
      cy.get('textarea[formControlName="description"]').type('A session description')
      cy.get('button[type="submit"]').should('not.be.disabled')
      cy.get('button[type="submit"]').click()
    })
  })
  