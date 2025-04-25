describe('Register spec', () => {
    it('Register succesfull', () => {
        cy.visit('/register')

        cy.intercept('POST', '/api/auth/register', {
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: true
            },
        })

        cy.intercept(
        {
            method: 'GET',
            url: '/api/session',
        },
        []).as('session')

        cy.get('input[formControlName=firstName]').type("toto")
        cy.get('input[formControlName=lastName]').type("toto")
        cy.get('input[formControlName=email]').type("yoga2@studio.com")
        cy.get('input[formControlName=password]').type("test!1234")

        cy.get('button[type=submit]').click()
    
        cy.url().should('include', '/login')
    })

    it('Submit button is disabled when email or password is empty', () => {
        cy.visit('/register')
      
        const email = () => cy.get('input[formControlName=email]')
        const password = () => cy.get('input[formControlName=password]')
        const firstName = () => cy.get('input[formControlName=firstName]')
        const lastName = () => cy.get('input[formControlName=lastName]')
        const submitButton = () => cy.get('button[type=submit]')

        // Cas 1: Tous les champs vides
        email().clear()
        password().clear()
        firstName().clear()
        lastName().clear()
        submitButton().should('be.disabled')

        // Cas 2: Email manquant
        password().type('password123')
        firstName().type('John')
        lastName().type('Doe')
        email().clear()
        submitButton().should('be.disabled')

        // Cas 3: Password manquant
        email().type('john@doe.com')
        password().clear()
        submitButton().should('be.disabled')

        // Cas 4: FirstName manquant
        password().type('password123')
        firstName().clear()
        submitButton().should('be.disabled')

        // Cas 5: LastName manquant
        firstName().type('John')
        lastName().clear()
        submitButton().should('be.disabled')

        // Cas 6: Tous les champs remplis → bouton activé
        lastName().type('Doe')
        submitButton().should('not.be.disabled')
      })
})