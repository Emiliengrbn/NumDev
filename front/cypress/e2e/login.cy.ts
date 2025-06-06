describe('Login spec', () => {
  it('Login successfull', () => {
    cy.login()
  })

  it('Login failed', () => {
    cy.visit('/login')
  
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: {
        message: 'An error occurred'
      },
    }).as('loginFail')
  
    // Remplir les champs avec de mauvaises infos
    cy.get('input[formControlName=email]').type("wrong@user.com")
    cy.get('input[formControlName=password]').type(`${"wrongpassword"}{enter}`)

    cy.get('button[type=submit]').click()
  
    // Vérification que l'on reste sur la page de login
    cy.url().should('include', '/login')
  
    // Vérification de la présence d'un message d'erreur (adapte le sélecteur si besoin)
    cy.contains('An error occurred').should('be.visible')
  })

  it('Login button is disabled when email or password is empty', () => {
    cy.visit('/login')
  
    // 1. Les deux champs sont vides
    cy.get('input[formControlName=email]').clear()
    cy.get('input[formControlName=password]').clear()
    cy.get('button[type=submit]').should('be.disabled')
  
    // 2. Email rempli, mot de passe vide
    cy.get('input[formControlName=email]').type('user@domain.com')
    cy.get('input[formControlName=password]').clear()
    cy.get('button[type=submit]').should('be.disabled')
  
    // 3. Email vide, mot de passe rempli
    cy.get('input[formControlName=email]').clear()
    cy.get('input[formControlName=password]').type('somePassword123')
    cy.get('button[type=submit]').should('be.disabled')
  
    // 4. Les deux champs remplis => le bouton doit être activé
    cy.get('input[formControlName=email]').type('user@domain.com')
    cy.get('button[type=submit]').should('not.be.disabled')
  })
  
  
});