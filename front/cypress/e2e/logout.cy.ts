describe('Logout spec', () => {
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
        
        cy.get('input[formControlName=email]').type('yogi@studio.com')
        cy.get('input[formControlName=password]').type('test!1234')
        cy.get('button[type=submit]').click()
        
        cy.url().should('include', '/sessions')
    })

    it('Logout succesfully', () => {
        cy.contains('.link', 'Logout').click()

        cy.url().should('eq', 'http://localhost:4200/')
    })
    
});