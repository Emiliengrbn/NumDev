describe('Logout spec', () => {
    beforeEach(() => {
        cy.login()
    })

    it('Logout succesfully', () => {
        cy.contains('.link', 'Logout').click()

        cy.url().should('eq', 'http://localhost:4200/')
    })
    
});