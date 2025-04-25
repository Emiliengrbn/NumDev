Cypress.Commands.add('login', () => {

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
  
    // Vérifie qu'on est redirigé vers /sessions
    cy.url().should('include', '/sessions')
})

Cypress.Commands.add('getTeachers', () => {
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
})

Cypress.Commands.add('getTeacher', () => {
    cy.intercept('get', '/api/teacher/1', {
        statusCode: 200,
        body: {
          id: 1,
          lastName: 'DELAHAYE',
          firstName: 'Margot',
          createdAt: '2025-04-24T14:20:04',
          updatedAt: '2025-04-24T14:20:04',
        },
      }).as('getTeacher')
})

Cypress.Commands.add('getSessionDetail', () => {
    cy.intercept('GET', '/api/session/1', {
        statusCode: 200,
        body: {
          id: 1,
          name: 'yoga du matin', // en minuscules, Angular pipe "titlecase" l'affiche correctement
          date: '2025-06-02T00:00:00.000+00:00',
          teacher_id: 1,
          description: 'Yoga pour bien commencer la journée',
          users: [],
          createdAt: '2025-04-24T14:21:16',
          updatedAt: '2025-04-28T14:21:17',
        },
      }).as('getSessionDetail')
})

Cypress.Commands.add('getSessions', () => {
    cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [
            {
                id: 1,
                name: 'yoga du matin', // en minuscules, Angular pipe "titlecase" l'affiche correctement
                date: '2025-06-02T00:00:00.000+00:00',
                teacher_id: 1,
                description: 'Yoga pour bien commencer la journée',
                users: [],
                createdAt: '2025-04-24T14:21:16',
                updatedAt: '2025-04-28T14:21:17',
              },
              {
                id: 2,
                name: 'pilates débutant', // en minuscules, Angular pipe "titlecase" l'affiche correctement
                date: '2025-06-02T00:00:00.000+00:00',
                teacher_id: 1,
                description: 'Yoga pour bien commencer la journée',
                users: [],
                createdAt: '2025-05-24T14:21:16',
                updatedAt: '2025-05-28T14:21:17',
              }
        ]
      }).as('getSessions')
})