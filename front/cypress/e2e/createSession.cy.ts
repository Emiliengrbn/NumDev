describe('Create session spec', () => {
    beforeEach(() => {

      // Intercepter la requête GET pour récupérer les enseignants avec le jeton dans les en-têtes
      cy.getTeachers()
  
      cy.login()
  
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
  